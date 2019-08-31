package com.santex.models.http;

import com.santex.models.entities.Competition;
import com.santex.models.entities.Team;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompAndTeamsResp {

    private Competition competition;
    private List<Team> teams;
}
