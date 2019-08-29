package com.santex.models.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Builder
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
    private String code;
    private String name;
    private Area area;

    @Column(name = "fully_imported")
    private boolean fullyImported;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "COMPETITIONS_TEAMS",
            joinColumns = { @JoinColumn(name = "competition_id") },
            inverseJoinColumns = { @JoinColumn(name = "team_id") })
    private List<Team> teams;
}
