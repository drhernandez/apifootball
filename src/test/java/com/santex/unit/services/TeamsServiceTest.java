package com.santex.unit.services;

import com.google.common.collect.Lists;
import com.santex.clients.FootballApiClient;
import com.santex.daos.TeamsDao;
import com.santex.exceptions.ApiException;
import com.santex.exceptions.InternalErrorException;
import com.santex.models.entities.Team;
import com.santex.services.imp.TeamsServiceImp;
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
public class TeamsServiceTest {

    @Mock
    Session session;
    @Mock
    Transaction transaction;
    @Mock
    SessionFactory sessionFactory;
    @Mock
    FootballApiClient footballApiClient;
    @Mock
    TeamsDao teamsDao;

    @InjectMocks
    TeamsServiceImp service;

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
        List<Team> teams = service.findByIds(new ArrayList<>());
        assertTrue(teams.isEmpty());
    }

    @Test(expected = InternalErrorException.class)
    public void findByIds_error() {
        when(teamsDao.findByIds(any())).thenThrow(new HibernateException("error"));
        service.findByIds(Lists.newArrayList(1L));
    }

    @Test
    public void findByIds_ok() {
        List<Long> ids = Lists.newArrayList(1L, 2L);
        List<Team> mock = new ArrayList<>();
        mock.add(Team.builder().id(1L).build());
        mock.add(Team.builder().id(2L).build());
        when(teamsDao.findByIds(ids)).thenReturn(mock);

        List<Team> teams = service.findByIds(ids);

        assertEquals(2, mock.size());
    }

    @Test
    public void getTeamsFromApi() {

        Team t1 = Team.builder().id(1L).build();
        Team t2 = Team.builder().id(2L).build();
        Team t3 = Team.builder().id(3L).build();

        List<Long> ids = Lists.newArrayList(1L, 2L, 3L, 4L, 5L);

        when(footballApiClient.getTeam(1L)).thenReturn(t1);
        when(footballApiClient.getTeam(2L)).thenReturn(t2);
        when(footballApiClient.getTeam(3L)).thenReturn(t3);
        when(footballApiClient.getTeam(4L)).thenThrow(new ApiException());
        when(footballApiClient.getTeam(5L)).thenThrow(new ApiException());

        List<Team> teams = service.getTeamsFromApi(ids);

        assertEquals(3, teams.size());
    }
}
