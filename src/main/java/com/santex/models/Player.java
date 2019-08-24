package com.santex.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@Entity
@Table(name = "PLAYERS")
public class Player {

    @Id
    @Column(name = "player_key")
    private Long playerKey;

    @Column(name = "player_name")
    private String playerName;

    @Column(name = "player_number")
    private String playerNumber;

    @Column(name = "player_country")
    private String playerCountry;

    @Column(name = "player_type")
    private String playerType;

    @Column(name = "player_age")
    private String playerAge;

    @Column(name = "player_match_played")
    private String playerMatchPlayed;

    @Column(name = "player_goals")
    private String playerGoals;

    @Column(name = "player_yellow_cards")
    private String playerYellowCards;

    @Column(name = "player_red_cards")
    private String playerRedCards;
}
