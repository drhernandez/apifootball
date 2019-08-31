package com.santex.services.imp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.clients.FootballApiClient;
import com.santex.daos.TeamsDao;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.models.entities.Team;
import com.santex.services.TeamsService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class TeamsServiceImp implements TeamsService {

    private final SessionFactory sessionFactory;
    private final FootballApiClient footballApiClient;
    private final TeamsDao teamsDao;

    @Inject
    public TeamsServiceImp(SessionFactory sessionFactory, FootballApiClient footballApiClient, TeamsDao teamsDao) {
        this.sessionFactory = sessionFactory;
        this.footballApiClient = footballApiClient;
        this.teamsDao = teamsDao;
    }

    @Override
    public List<Team> findByIds(List<Long> teamIds) {

        if (teamIds == null) throw new IllegalArgumentException();

        List<Team> teams = new ArrayList<>();

        if (teamIds.isEmpty()) {
            return teams;
        }

        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            teams = teamsDao.findByIds(teamIds);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("[message: Error getting teams ({})] [error: {}]", Arrays.toString(teamIds.toArray()), ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException("Error getting teams", e);
        }

        return teams;
    }

    @Override
    public List<Team> getTeamsFromApi(List<Long> teamIds) {

        List<CompletableFuture<Team>> futures = new ArrayList<>();
        teamIds.forEach(teamId -> futures.add(
                CompletableFuture
                        .supplyAsync(() -> footballApiClient.getTeam(teamId))
                        .handle((result, error) -> {
                            if (error != null) {
                                logger.error("[message: Error getting team {}] [error: {}]", teamId, error);
                                return null;
                            } else {
                                return result;
                            }
                        })
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
        return futures.stream().map(CompletableFuture::join).filter(Objects::nonNull).collect(Collectors.toList());
    }
}