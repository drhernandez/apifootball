package com.santex.unit.daos;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.santex.configs.DataBaseTestConfigs;
import com.santex.configs.Injectors;
import com.santex.daos.TeamsDao;
import com.santex.daos.imp.TeamsDaoImp;
import com.santex.models.entities.Team;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.santex.configs.Injectors.APP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TeamsDaoTest {

    private static SessionFactory sessionFactory;
    private TeamsDao teamsDao;
    private Transaction transaction;

    @BeforeClass
    public static void beforeAll() {
        Injector injector = Guice.createInjector(new DataBaseTestConfigs());
        Injectors.addInjector(APP, injector);
        sessionFactory = Injectors.getInjector(APP).getInstance(SessionFactory.class);

        Team t1 = Team.builder().id(1L).name("AC Milan").build();
        Team t2 = Team.builder().id(2L).name("ACF Fiorentina").build();


        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(t1);
        session.save(t2);
        tx.commit();
    }

    @Before
    public void setUp() {
        teamsDao = new TeamsDaoImp(sessionFactory);
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @After
    public void clearUp() {
        transaction.rollback();
    }

    @Test
    public void findByIds() {
        List<Team> teams = teamsDao.findByIds(Lists.newArrayList(1L, 2L));

        assertEquals(2, teams.size());
    }

    @Test
    public void findByIdsNotFound() {

        List<Team> teams = teamsDao.findByIds(Lists.newArrayList(3L, 5L));

        assertTrue(teams.isEmpty());
    }
}
