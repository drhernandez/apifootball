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
     * This service imports a league with their teams and players and store it on the DB.
     * Due to the rate limit of the API, a competition could ends up with its teams partially imported.
     * You may have to import the competition many times in order to fully import all its teams.
     * @param competitionCode
     * @return the competition
     */
    Competition importLeague(String competitionCode);
}
