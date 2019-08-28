package com.santex.daos;

import com.santex.models.entities.Competition;

public interface CompetitionsDao extends GenericDao<Competition> {

    /**
     * Find a competition by code
     * @param competitionCode
     * @return
     */
    Competition findByCode(String competitionCode);
}
