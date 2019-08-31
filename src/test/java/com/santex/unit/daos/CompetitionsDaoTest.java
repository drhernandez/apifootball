package com.santex.unit.daos;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.santex.configs.DataBaseTestConfigs;
import com.santex.configs.Injectors;
import com.santex.daos.CompetitionsDao;
import com.santex.daos.imp.CompetitionsDaoImp;
import com.santex.models.entities.Competition;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.NoResultException;

import static com.santex.configs.Injectors.APP;
import static org.junit.Assert.assertEquals;

public class CompetitionsDaoTest {

    private static SessionFactory sessionFactory;
    private CompetitionsDao competitionsDao;
    private Transaction transaction;

    @BeforeClass
    public static void beforeAll() {
        Injector injector = Guice.createInjector(new DataBaseTestConfigs());
        Injectors.addInjector(APP, injector);
        sessionFactory = Injectors.getInjector(APP).getInstance(SessionFactory.class);

        Competition c1 = Competition.builder().id(1L).code("CL").name("champions league").build();
        Competition c2 = Competition.builder().id(2L).code("PL").name("premier league").build();


        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(c1);
        session.save(c2);
        tx.commit();
    }

    @Before
    public void setUp() {
        competitionsDao = new CompetitionsDaoImp(sessionFactory);
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @After
    public void clearUp() {
        transaction.rollback();
    }

    @Test
    public void findByCode() {
        Competition competition = competitionsDao.findByCode("CL");

        assertEquals("champions league", competition.getName());
    }

    @Test(expected = NoResultException.class)
    public void findByCodeNotFound() {
        competitionsDao.findByCode("SA");
    }
}
