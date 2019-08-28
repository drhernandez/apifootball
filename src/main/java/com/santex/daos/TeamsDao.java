package com.santex.daos;

import com.santex.models.entities.Team;

import java.util.List;

public interface TeamsDao extends GenericDao<Team> {

    /**
     * Find list of teams by id
     * @param ids
     * @return
     */
    List<Team> findByIds(List<Long> ids);
}
