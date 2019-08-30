package com.santex.models.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.hibernate.annotations.CascadeType.SAVE_UPDATE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TEAMS")
public class Team implements Serializable {

    @Id
    private Long id;

    private String name;

    private String tla;

    private Area area;

    @Column(name = "short_name")
    private String shortName;

    @ManyToMany(mappedBy = "teams")
    private Set<Competition> competitions;

    @JsonAlias({"squad"})
    @Cascade(SAVE_UPDATE)
    @ManyToMany
    @JoinTable(name = "TEAMS_PLAYERS",
            joinColumns = { @JoinColumn(name = "team_id") },
            inverseJoinColumns = { @JoinColumn(name = "player_id") })
    private Set<Player> players;


    public Set<Player> getPlayers() {
        if (players == null) {
            players = new HashSet<>();
        }
        return players;
    }

    public void addPlayer(Player player) {
        this.getPlayers().add(player);
        player.getTeams().add(this);
    }

    public void removePlayer(Player player) {
        this.getPlayers().remove(player);
        player.getTeams().remove(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Team team = (Team) o;

        return id.equals(team.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}