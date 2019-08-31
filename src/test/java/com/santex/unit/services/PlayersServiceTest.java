package com.santex.unit.services;

import com.google.common.collect.Lists;
import com.santex.daos.PlayersDao;
import com.santex.exceptions.InternalErrorException;
import com.santex.exceptions.NotFoundException;
import com.santex.models.entities.Competition;
import com.santex.models.entities.Player;
import com.santex.services.CompetitionsService;
import com.santex.services.imp.PlayersServiceImp;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayersServiceTest {

    @Mock
    Session session;
    @Mock
    Transaction transaction;
    @Mock
    SessionFactory sessionFactory;
    @Mock
    CompetitionsService competitionsService;
    @Mock
    PlayersDao playersDao;

    @InjectMocks
    PlayersServiceImp service;

    @Before
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByIds_nullIds() {
        service.findByIds(null);
    }

    @Test
    public void findByIds_emptyList() {
        List<Player> players = service.findByIds(new ArrayList<>());
        assertTrue(players.isEmpty());
    }

    @Test(expected = InternalErrorException.class)
    public void findByIds_error() {
        when(playersDao.findByIds(any())).thenThrow(new HibernateException("error"));
        service.findByIds(Lists.newArrayList(1L));
    }

    @Test
    public void findByIds_ok() {
        List<Long> ids = Lists.newArrayList(1L, 2L);
        List<Player> mock = new ArrayList<>();
        mock.add(Player.builder().id(1L).build());
        mock.add(Player.builder().id(2L).build());
        mock.add(Player.builder().id(3L).build());
        when(playersDao.findByIds(ids)).thenReturn(mock);

        List<Player> players = service.findByIds(ids);

        assertEquals(3, players.size());
    }

    @Test(expected = NotFoundException.class)
    public void countPlayersByCompetition_notFound() {
        when(competitionsService.findByCode("CL", false)).thenReturn(null);
        service.countPlayersByCompetition("CL");
    }

    @Test(expected = InternalErrorException.class)
    public void countPlayersByCompetition_error() {
        when(competitionsService.findByCode("CL", false)).thenReturn(new Competition());
        when(playersDao.countPlayersByCompetition("CL")).thenThrow(new HibernateException("error"));
        service.countPlayersByCompetition("CL");
    }

    @Test
    public void countPlayersByCompetition_ok() {
        when(competitionsService.findByCode("CL", false)).thenReturn(new Competition());
        when(playersDao.countPlayersByCompetition("CL")).thenReturn(5L);
        long count = service.countPlayersByCompetition("CL");
        assertEquals(5, count);
    }
}
