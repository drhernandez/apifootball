package com.santex.unit.daos;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.santex.configs.DataBaseTestConfigs;
import com.santex.configs.Injectors;
import com.santex.daos.PlayersDao;
import com.santex.daos.imp.PlayersDaoImp;
import com.santex.models.entities.Competition;
import com.santex.models.entities.Player;
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

public class PlayersDaoTest {

    private static SessionFactory sessionFactory;
    private PlayersDao playersDao;
    private Transaction transaction;

    @BeforeClass
    public static void beforeAll() {
        Injector injector = Guice.createInjector(new DataBaseTestConfigs());
        Injectors.addInjector(APP, injector);
        sessionFactory = Injectors.getInjector(APP).getInstance(SessionFactory.class);

        Competition c1 = Competition.builder().id(1L).code("CL").name("c 1").build();
        Competition c2 = Competition.builder().id(2L).code("PL").name("c 2").build();
        Team t1 = Team.builder().id(1L).competitions(Sets.newHashSet(c1)).build();
        c1.setTeams(Sets.newHashSet(t1));
        Player p1 = Player.builder().id(1L).name("player 1").build();
        Player p2 = Player.builder().id(2L).name("player 2").build();
        t1.addPlayer(p1);
        t1.addPlayer(p2);

        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(c1);
        session.save(c2);
        tx.commit();
    }

    @Before
    public void setUp() {
        playersDao = new PlayersDaoImp(sessionFactory);
        transaction = sessionFactory.getCurrentSession().beginTransaction();
    }

    @After
    public void clearUp() {
        transaction.rollback();
    }

    @Test
    public void findByIds() {
        List<Player> result = playersDao.findByIds(Lists.newArrayList(1L));

        assertEquals(1, result.size());
        assertEquals("player 1", result.get(0).getName());
    }

    @Test
    public void findByIdsNotFound() {
        assertTrue(playersDao.findByIds(Lists.newArrayList(3L)).isEmpty());
    }

    @Test
    public void countPlayersByCompetition() {
        long count = playersDao.countPlayersByCompetition("CL");
        assertEquals(2, count);
    }
}
