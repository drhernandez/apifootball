package com.santex.services.imp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.clients.FootballApiClient;
import com.santex.daos.CompetitionsDao;
import com.santex.daos.TeamsDao;
import com.santex.enums.ErrorCodes;
import com.santex.exceptions.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    public boolean importLeague(String competitionCode) {

        //Search on DB
        Competition competition = findByCode(competitionCode);
        if (competition != null) {
            logger.error("[message: Competition {} already imported]", competitionCode);
            throw new ApiException(ErrorCodes.internal_error.name(), "League already imported", HttpStatus.SC_CONFLICT);
        }

        //Search on API
        CompAndTeamsResp response = footballApiClient.getCompetitionTeams(competitionCode);
        competition = response.getCompetition();
        List<Long> teamIds = response.getTeams().stream().map(team -> team.getId()).collect(Collectors.toList());
        List<Team> teams = teamsService.findByIds(teamIds);

        //Get missing teams from API
        teamIds.removeAll(teams.stream().map(team ->team.getId()).collect(Collectors.toList()));
        List<Team> missingTeams = bulkGetTeams(teamIds);
        teams.addAll(missingTeams);

        competition.setTeams(teams);
        saveOrUpdate(competition);

        return true;
    }

    private List<Team> bulkGetTeams(List<Long> teamIds) {

        List<CompletableFuture<Team>> futures = new ArrayList<>();
        teamIds.forEach(teamId -> futures.add(
                CompletableFuture.supplyAsync(() -> footballApiClient.getTeam(teamId))
        ));

        CompletableFuture allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        try {
            allFutures.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("[message: Thread interrupted] [error: {}]", ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException(e);
        } catch (ExecutionException e) {
            logger.error("[message: Promise execution error] [error: {}]", ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException(e);
        } catch (TimeoutException e) {
            logger.error("[message: Promise execution timeout]");
            throw new InternalErrorException(e);
        }

        //Get all responses
        List<Team> responses = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());

        //TODO Check if something went wrong
        return responses;
    }
}
