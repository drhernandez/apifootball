package com.santex.services.imp;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.santex.daos.TeamsDao;
import com.santex.exceptions.ExceptionUtils;
import com.santex.exceptions.InternalErrorException;
import com.santex.models.entities.Team;
import com.santex.services.TeamsService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Singleton
public class TeamsServiceImp implements TeamsService {

    SessionFactory sessionFactory;
    TeamsDao teamsDao;

    @Inject
    public TeamsServiceImp(SessionFactory sessionFactory, TeamsDao teamsDao) {
        this.sessionFactory = sessionFactory;
        this.teamsDao = teamsDao;
    }

    @Override
    public List<Team> findByIds(List<Long> teamIds) {

        List<Team> teams;
        Transaction tx = sessionFactory.getCurrentSession().beginTransaction();
        try {
            teams = teamsDao.findByIds(teamIds);
            tx.commit();
        } catch (NoResultException e) {
            tx.rollback();
            logger.error("[message: Teams not found. Ids: ({})]", Arrays.toString(teamIds.toArray()));
            teams = new ArrayList<>();
        } catch (HibernateException e) {
            tx.rollback();
            logger.error("[message: Error getting teams ({})] [error: {}]", Arrays.toString(teamIds.toArray()), ExceptionUtils.getLogMessage(e));
            throw new InternalErrorException("Error getting teams", e);
        }

        return teams;
    }
}