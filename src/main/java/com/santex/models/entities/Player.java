package com.santex.models.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santex.json.deserializers.OffsetDateTimeDeserializer;
import com.santex.json.serializers.OffsetDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PLAYERS")
public class Player implements Serializable {

    @Id
    private Long id;

    private String name;

    private String position;

    private String nationality;

    @JsonSerialize(using = OffsetDateTimeSerializer.class)
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    @Column(name = "date_of_birth")
    private OffsetDateTime dateOfBirth;

    @Column(name = "country_of_birth")
    private String countryOfBirth;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "players")  //Could belong to two teams ej: Messi -> [Argentina, FC Barcelona]
    private Set<Team> teams;


    public Set<Team> getTeams() {
        if (teams == null) {
            teams = new HashSet<>();
        }
        return teams;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
