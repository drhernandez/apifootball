package com.santex.services.imp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.clients.FootballApiClient;
import com.santex.daos.CompetitionsDao;
import com.santex.exceptions.ApiException;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.ServerErrorException;
import com.santex.models.entities.Competition;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import com.santex.services.CompetitionsService;
import com.santex.services.TeamsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class CompetitionsServiceImp implements CompetitionsService {

    private final SessionFactory sessionFactory;
    private final TeamsService teamsService;
    private final CompetitionsDao competitionsDao;
    private final FootballApiClient footballApiClient;

    @Inject
    public CompetitionsServiceImp(SessionFactory sessionFactory, TeamsService teamsService, CompetitionsDao competitionsDao, FootballApiClient footballApiClient) {
        this.sessionFactory = sessionFactory;
        this.teamsService = teamsService;
        this.competitionsDao = competitionsDao;
        this.footballApiClient = footballApiClient;
    }

    @Override
    public Competition findByCode(String competitionCode) {

        Competition competition;
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            competition = competitionsDao.findByCode(competitionCode);
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
            throw new ServerErrorException(e);
        }
    }

    @Override
    public Competition importLeague(String competitionCode) {

        //Search on DB
        Competition competition = findByCode(competitionCode);
        if (competition != null && competition.isFullyImported()) {
            logger.error("[message: Competition {} already imported]", competitionCode);
            throw new ApiException("League already imported", HttpStatus.SC_CONFLICT);
        }

        //Search on API
        CompAndTeamsResp response = footballApiClient.getCompetitionTeams(competitionCode);
        competition = response.getCompetition();
        List<Long> teamIds = response.getTeams().stream().map(team -> team.getId()).collect(Collectors.toList());
        List<Team> teams = teamsService.findByIds(teamIds);

        //Get missing teams from API
        teamIds.removeAll(teams.stream().map(team ->team.getId()).collect(Collectors.toList()));
        List<Team> missingTeams = teamsService.getTeamsFromApi(teamIds);

        //Set the relation between team and player
        missingTeams.forEach(team -> team.getPlayers().forEach(player -> player.setTeam(team)));
        teams.addAll(missingTeams);
        competition.setTeams(teams);

        //Check if is fully imported due to API rate limmit
        competition.setFullyImported(response.getTeams().size() == teams.size());

        saveOrUpdate(competition);

        return competition;
    }
}
