package com.santex.services.imp;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.clients.FootballApiClient;
import com.santex.daos.CompetitionsDao;
import com.santex.exceptions.ApiException;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.models.entities.Competition;
import com.santex.models.entities.Player;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import com.santex.services.CompetitionsService;
import com.santex.services.PlayersService;
import com.santex.services.TeamsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class CompetitionsServiceImp implements CompetitionsService {

    private final SessionFactory sessionFactory;
    private final TeamsService teamsService;
    private final PlayersService playersService;
    private final CompetitionsDao competitionsDao;
    private final FootballApiClient footballApiClient;

    @Inject
    public CompetitionsServiceImp(SessionFactory sessionFactory, TeamsService teamsService, PlayersService playersService, CompetitionsDao competitionsDao, FootballApiClient footballApiClient) {
        this.sessionFactory = sessionFactory;
        this.teamsService = teamsService;
        this.playersService = playersService;
        this.competitionsDao = competitionsDao;
        this.footballApiClient = footballApiClient;
    }

    @Override
    public Competition findByCode(String competitionCode, boolean initializeTeams) {

        Competition competition;
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            competition = competitionsDao.findByCode(competitionCode);
            if (initializeTeams) {
                Hibernate.initialize(competition.getTeams());
            }
            tx.commit();
        } catch (NoResultException e) {
            tx.rollback();
            logger.error("[message: Competition {} not found]", competitionCode);
            competition = null;
        } catch (HibernateException e) {
            tx.rollback();
            logger.error("[message: Error getting competition {}] [error: {}]", competitionCode, ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException("Error getting competition", e);
        }

        return competition;
    }

    @Override
    public void saveOrUpdate(Competition competition) {

        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            competitionsDao.saveOrUpdate(competition);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("[message: Error saving competition {}] [error: {}]", competition.getId(), ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException(e);
        }
    }

    @Override
    public Competition importLeague(String competitionCode) {

        //Search on DB
        Competition competition = findByCode(competitionCode, true);
        if (competition != null && competition.isFullyImported()) {
            logger.error("[message: Competition {} already imported]", competitionCode);
            throw new ApiException("League already imported", HttpStatus.SC_CONFLICT);
        }

        //Search on API
        CompAndTeamsResp response = footballApiClient.getCompetitionTeams(competitionCode);
        if (competition == null) {
            competition = response.getCompetition();
        }

        //Avoid teams already imported
        List<Long> existingTeamIds = competition.getTeams().stream().map(Team::getId).collect(Collectors.toList());
        List<Long> teamIds = response.getTeams().stream().map(Team::getId).collect(Collectors.toList());
        teamIds.removeAll(existingTeamIds);

        //Find teams on DB loaded by other competitions and remove them from the missing ids list
        List<Team> teams = teamsService.findByIds(teamIds);
        teamIds.removeAll(teams.stream().map(Team::getId).collect(Collectors.toList()));

        //Get missing teams from API
        List<Team> missingTeams = teamsService.getTeamsFromApi(teamIds);
        mergeExistingTeamsWithMissingTeams(teams, missingTeams);

        //Check if is fully imported due to API rate limmit
        competition.getTeams().addAll(teams);
        competition.setFullyImported(response.getTeams().size() == competition.getTeams().size());
        saveOrUpdate(competition);

        return competition;
    }

    private void mergeExistingTeamsWithMissingTeams(List<Team> existingTeams, List<Team> missingTeams) {

        //Map existing players with new teams and check if a player is already loaded by other team
        missingTeams.forEach(team -> {

            //Transform List<Player> -> Map<Id, Player> for better use
            Map<Long, Player> playersMap = team.getPlayers().stream().collect(Collectors.toMap(Player::getId, player -> player));
            List<Player> existingPlayers = playersService.findByIds(Lists.newArrayList(playersMap.keySet()));

            //Override map with players that already exist on the DB
            if (!existingPlayers.isEmpty()) {
                existingPlayers.forEach(player -> playersMap.put(player.getId(), player));
            }

            //Add players with proper relation
            team.getPlayers().clear();
            playersMap.forEach((id, player) -> team.addPlayer(player));

        });
        existingTeams.addAll(missingTeams);
    }
}
