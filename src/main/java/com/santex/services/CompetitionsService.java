package com.santex.services;

import com.santex.models.entities.Competition;

public interface CompetitionsService {

    /**
     * Search competition on DB
     * @param competitionCode
     * @return
     */
    Competition findByCode(String competitionCode);

    /**
     * Save or update the given competition on the DB
     * @param competition
     */
    void saveOrUpdate(Competition competition);

    /**
     * This service imports a league with their teams and players and store ir on the DB.
     * @param competitionCode
     * @return true if league is imported properly
     */
    boolean importLeague(String competitionCode);
}
