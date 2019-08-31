package com.santex.services;

import com.santex.exceptions.NotFoundException;
import com.santex.models.entities.Player;

import java.util.List;

public interface PlayersService {

    /**
     * Get list of players by id
     * @param ids
     * @return
     */
    List<Player> findByIds(List<Long> ids);

    /**
     * Count all players of the given competition
     * @param competitionCode
     * @return number of players
     * @throws NotFoundException if competition is not found on the DB
     */
    long countPlayersByCompetition(String competitionCode) throws NotFoundException;
}
