package com.santex.unit.services;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.santex.TestUtils;
import com.santex.clients.FootballApiClient;
import com.santex.daos.CompetitionsDao;
import com.santex.exceptions.ApiException;
import com.santex.exceptions.InternalErrorException;
import com.santex.models.entities.Competition;
import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;
import com.santex.services.PlayersService;
import com.santex.services.TeamsService;
import com.santex.services.imp.CompetitionsServiceImp;
import com.santex.utils.MapperUtils;
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

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompetitionsServiceTest {

    @Mock
    Session session;
    @Mock
    Transaction transaction;
    @Mock
    SessionFactory sessionFactory;
    @Mock
    TeamsService teamsService;
    @Mock
    PlayersService playersService;
    @Mock
    CompetitionsDao competitionsDao;
    @Mock
    FootballApiClient footballApiClient;

    @InjectMocks
    CompetitionsServiceImp service;

    @Before
    public void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }

    @Test
    public void findByCode_NoResult() {
        when(competitionsDao.findByCode("CL")).thenThrow(new NoResultException());
        Competition competition = service.findByCode("CL", false);
        assertNull(competition);
    }

    @Test(expected = InternalErrorException.class)
    public void findByCode_error() {
        when(competitionsDao.findByCode("CL")).thenThrow(new HibernateException("error"));
        service.findByCode("CL", false);
    }

    @Test
    public void findByCode_ok() {
        when(competitionsDao.findByCode("CL")).thenReturn(Competition.builder().id(1L).code("CL").build());
        Competition competition = service.findByCode("CL", false);

        assertEquals(1, competition.getId().longValue());
        assertEquals("CL", competition.getCode());
    }

    @Test(expected = InternalErrorException.class)
    public void saveorUpdate_error() {
        doThrow(new HibernateException("error")).when(competitionsDao).saveOrUpdate(any());
        service.saveOrUpdate(Competition.builder().build());
    }

    @Test(expected = ApiException.class)
    public void importLeague_alreadyImported() {
        when(competitionsDao.findByCode("CL")).thenReturn(Competition.builder().id(1L).code("CL").fullyImported(true).build());
        service.importLeague("CL");
    }

    @Test
    public void importLeague_newCompetition() {
        CompAndTeamsResp compAndTeamsRespMock = MapperUtils.toObject(TestUtils.getJsonString("/mocks/compAndTeams", "getCompetitionAndTeams_ok.json"), CompAndTeamsResp.class);
        List<Team> foundedTeamsMock = Lists.newArrayList(
                Team.builder().id(57L).name("Arsenal FC").build()
        );
        List<Team> missingTeamsMock = Lists.newArrayList(
                Team.builder().id(58L).name("Aston Villa FC").build(),
                Team.builder().id(61L).name("Chelsea FC").build(),
                Team.builder().id(62L).name("Everton FC").build(),
                Team.builder().id(64L).name("Liverpool FC").build()
        );
        when(competitionsDao.findByCode("PL")).thenThrow(new NoResultException());
        when(footballApiClient.getCompetitionTeams("PL")).thenReturn(compAndTeamsRespMock);
        when(teamsService.findByIds(compAndTeamsRespMock.getTeams().stream().map(Team::getId).collect(Collectors.toList()))).thenReturn(foundedTeamsMock);
        when(teamsService.getTeamsFromApi(Lists.newArrayList(58L, 61L, 62L, 64L))).thenReturn(missingTeamsMock);
        when(playersService.findByIds(any())).thenReturn(new ArrayList<>());

        Competition competition = service.importLeague("PL");

        assertNotNull(competition);
        assertEquals("PL", competition.getCode());
        assertEquals(5, competition.getTeams().size());
        assertTrue(competition.isFullyImported());
    }

    @Test
    public void importLeague_partialImport() {
        CompAndTeamsResp compAndTeamsRespMock = MapperUtils.toObject(TestUtils.getJsonString("/mocks/compAndTeams", "getCompetitionAndTeams_ok.json"), CompAndTeamsResp.class);
        List<Team> foundedTeamsMock = Lists.newArrayList(
                Team.builder().id(57L).name("Arsenal FC").build()
        );
        List<Team> missingTeamsMock = Lists.newArrayList(
                Team.builder().id(58L).name("Aston Villa FC").build(),
                Team.builder().id(61L).name("Chelsea FC").build()
        );
        when(competitionsDao.findByCode("PL")).thenThrow(new NoResultException());
        when(footballApiClient.getCompetitionTeams("PL")).thenReturn(compAndTeamsRespMock);
        when(teamsService.findByIds(compAndTeamsRespMock.getTeams().stream().map(Team::getId).collect(Collectors.toList()))).thenReturn(foundedTeamsMock);
        when(teamsService.getTeamsFromApi(Lists.newArrayList(58L, 61L, 62L, 64L))).thenReturn(missingTeamsMock);
        when(playersService.findByIds(any())).thenReturn(new ArrayList<>());

        Competition competition = service.importLeague("PL");

        assertNotNull(competition);
        assertEquals("PL", competition.getCode());
        assertEquals(3, competition.getTeams().size());
        assertFalse(competition.isFullyImported());
    }

    @Test
    public void importLeague_compleateImport() {
        Competition competitionMock = Competition.builder().id(1L).code("PL").fullyImported(false).teams(
                Sets.newHashSet(
                        Team.builder().id(57L).name("Arsenal FC").build(),
                        Team.builder().id(58L).name("Aston Villa FC").build(),
                        Team.builder().id(61L).name("Chelsea FC").build()
                )
        ).build();
        CompAndTeamsResp compAndTeamsRespMock = MapperUtils.toObject(TestUtils.getJsonString("/mocks/compAndTeams", "getCompetitionAndTeams_ok.json"), CompAndTeamsResp.class);
        List<Team> missingTeamsMock = Lists.newArrayList(
                Team.builder().id(62L).name("Everton FC").build(),
                Team.builder().id(64L).name("Liverpool FC").build()
        );
        when(competitionsDao.findByCode("PL")).thenReturn(competitionMock);
        when(footballApiClient.getCompetitionTeams("PL")).thenReturn(compAndTeamsRespMock);
        when(teamsService.getTeamsFromApi(Lists.newArrayList(62L, 64L))).thenReturn(missingTeamsMock);
        when(playersService.findByIds(any())).thenReturn(new ArrayList<>());

        Competition competition = service.importLeague("PL");

        assertNotNull(competition);
        assertEquals("PL", competition.getCode());
        assertEquals(5, competition.getTeams().size());
        assertTrue(competition.isFullyImported());
    }
}
