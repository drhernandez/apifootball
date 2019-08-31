package com.santex.services;

import com.santex.models.entities.Team;

import java.util.List;

public interface TeamsService {

    /**
     * Find teams on the DB by list of ids
     * @param teamIds
     * @return
     */
    List<Team> findByIds(List<Long> teamIds);

    /**
     * Get a list of teams from the API
     * @param teamIds
     * @return
     */
    List<Team> getTeamsFromApi(List<Long> teamIds);
}
