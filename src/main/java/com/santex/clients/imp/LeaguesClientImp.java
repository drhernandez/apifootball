package com.santex.clients.imp;

import com.google.inject.Inject;
import com.santex.clients.LeaguesClient;
import kong.unirest.UnirestInstance;

import javax.inject.Named;

public class LeaguesClientImp implements LeaguesClient {

    public static final String CLIENT_NAME = "leagues-client";
    private final UnirestInstance instance;

    @Inject
    public LeaguesClientImp(@Named(CLIENT_NAME) UnirestInstance instance) {
        this.instance = instance;
    }
}
