package com.santex.services;

import com.santex.exceptions.NotFoundException;

public interface PlayersService {

    /**
     * Count all players of the given competition
     * @param competitionCode
     * @return number of players
     * @throws NotFoundException if competition is not found on the DB
     */
    long countPlayersByCompetition(String competitionCode) throws NotFoundException;
}
