package com.santex.services.imp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.daos.PlayersDao;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.NotFoundException;
import com.santex.services.CompetitionsService;
import com.santex.services.PlayersService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

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
    public long countPlayersByCompetition(String competitionCode) throws NotFoundException {

        if (competitionsService.findByCode(competitionCode) == null) {
            logger.error("[message: Error counting players of {}] [error: Competition not found]", competitionCode);
            throw new NotFoundException();
        }

        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        long count;
        try {
            count = playersDao.countPlayersByCompetition(competitionCode);
            tx.commit();
        } catch (Exception e) {
            logger.error("[message: Error counting players of {}] [error: {}]", competitionCode, ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException();
        }

        return count;
    }
}
