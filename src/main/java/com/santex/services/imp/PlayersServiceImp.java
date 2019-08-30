package com.santex.services.imp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.daos.PlayersDao;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.NotFoundException;
import com.santex.models.entities.Player;
import com.santex.services.CompetitionsService;
import com.santex.services.PlayersService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Singleton
public class PlayersServiceImp implements PlayersService {

    SessionFactory sessionFactory;
    CompetitionsService competitionsService;
    PlayersDao playersDao;

    @Inject
    public PlayersServiceImp(SessionFactory sessionFactory, CompetitionsService competitionsService, PlayersDao playersDao) {
        this.sessionFactory = sessionFactory;
        this.competitionsService = competitionsService;
        this.playersDao = playersDao;
    }

    @Override
    public List<Player> findByIds(List<Long> ids) {

        List<Player> players;
        if (ids.isEmpty()) {

            players = new ArrayList<>();

        } else {

            Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
            try {
                players = playersDao.findByIds(ids);
                tx.commit();
            } catch (Exception e) {
                tx.rollback();
                logger.error("[message: Error getting players {}] [error: {}]", Arrays.toString(ids.toArray()), ExceptionUtils.getLogMessage(e));
                throw new InternalErrorException();
            }
        }

        return players;
    }

    @Override
    public long countPlayersByCompetition(String competitionCode) throws NotFoundException {

        if (competitionsService.findByCode(competitionCode, false) == null) {
            logger.error("[message: Error counting players of {}] [error: Competition not found]", competitionCode);
            throw new NotFoundException();
        }

        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        long count;
        try {
            count = playersDao.countPlayersByCompetition(competitionCode);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            logger.error("[message: Error counting players of {}] [error: {}]", competitionCode, ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException();
        }

        return count;
    }
}
