package com.santex.services;

import com.santex.models.entities.Team;

import java.util.List;

public interface TeamsService {

    /**
     * Find teams by list of ids
     * @param teamIds
     * @return
     */
    List<Team> findByIds(List<Long> teamIds);
}
