package com.santex.daos;

import com.santex.exceptions.NotFoundException;
import com.santex.models.entities.Player;

public interface PlayersDao extends GenericDao<Player> {

    /**
     * Count all players of the given competition
     * @param competitionCode
     * @return number of players
     * @throws NotFoundException if competition is not found on the DB
     */
    long countPlayersByCompetition(String competitionCode) throws NotFoundException;
}
