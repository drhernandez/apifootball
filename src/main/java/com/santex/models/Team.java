package com.santex.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Builder
@Getter
@Setter
@Entity
@Table(name = "TEAMS")
public class Team {

    @Id
    @Column(name = "team_key")
    String teamKey;

    @Column(name = "team_name")
    String teamName;

    @Column(name = "team_badge")
    String teamBadge;

//    List<Player> players;
//    List<Coach> coaches;
}