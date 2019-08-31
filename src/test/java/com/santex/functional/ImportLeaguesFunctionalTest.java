package com.santex.functional;

import com.santex.TestUtils;
import com.santex.configs.Injectors;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.MockServerRule;

import static com.santex.configs.Injectors.APP;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ImportLeaguesFunctionalTest extends FunctionalTest {

    private Transaction transaction;
    private SessionFactory sessionFactory;
    private ClientAndServer mockServer;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);

    @Before
    public void setUp() {
        sessionFactory = Injectors.getInjector(APP).getInstance(SessionFactory.class);
        transaction = sessionFactory.getCurrentSession().beginTransaction();
        mockServer = startClientAndServer(1080);
    }

    @After
    public void clearUp() {
        transaction.rollback();
        mockServer.stop();
    }

    @Test
    public void importLeagues() {

        MockServerClient client = new MockServerClient("127.0.0.1", 1080);
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/competitions/PL/teams"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(TestUtils.getJsonString("/mocks/compAndTeams", "getCompetitionAndTeams_ok.json"))
                );
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/teams/57"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(TestUtils.getJsonString("/mocks/team", "57.json"))
                );
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/teams/58"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(TestUtils.getJsonString("/mocks/team", "58.json"))
                );
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/teams/61"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(TestUtils.getJsonString("/mocks/team", "61.json"))
                );
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/teams/62"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(TestUtils.getJsonString("/mocks/team", "62.json"))
                );
        client.when(
                request()
                        .withMethod("GET")
                        .withPath("/teams/64"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withBody(TestUtils.getJsonString("/mocks/team", "64.json"))
                );

        given().get("/import-league/PL")
                .then()
                .statusCode(201)
                .body(equalTo("{\"message\": \"Successfully imported\"}"));
    }
}
