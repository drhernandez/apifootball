package com.santex.models.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.NaturalId;

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
@Table(name = "COMPETITIONS",
        indexes = {@Index(name = "idx_competition_code", columnList = "code")})
public class Competition implements Serializable {


    @Id
    private Long id;

    @NaturalId
    private String code;

    private String name;

    private Area area;

    @Column(name = "fully_imported")
    private boolean fullyImported;

    @Cascade(SAVE_UPDATE)
    @ManyToMany
    @JoinTable(name = "COMPETITIONS_TEAMS",
            joinColumns = { @JoinColumn(name = "competition_id") },
            inverseJoinColumns = { @JoinColumn(name = "team_id") })
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

        Competition that = (Competition) o;

        if (!id.equals(that.id)) return false;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + code.hashCode();
        return result;
    }
}
