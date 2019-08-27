package com.santex.clients;

import com.santex.models.entities.Team;
import com.santex.models.http.CompAndTeamsResp;

public interface FootballApiClient {

    /**
     * Get competition and team info by id
     * @param competitionId (could be id or code)
     * @return
     */
    CompAndTeamsResp getCompetitionTeams(String competitionId);

    /**
     * Get full team info
     * @param teamId
     * @return
     */
    Team getTeam(Long teamId);
}
