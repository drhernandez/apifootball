package com.santex.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name = "COMPETITIONS")
public class Competition {

    @Id
    @Column(name = "league_id")
    private String leagueId;

    @Column(name = "league_name")
    private String leagueName;

    @Column(name = "country_id")
    private String countryId;

    @Column(name = "country_name")
    private String countryName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_key", foreignKey = @ForeignKey(name = "fk_COMPETITIONS_TEAMS"))
    private List<Team> teams;
}
