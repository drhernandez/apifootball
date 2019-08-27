package com.santex.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPETITIONS")
public class Competition {

    @Id
    private Long id;
    private String code;
    private String name;
    private Area area;

    @ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinTable(name = "COMPETITIONS_TEAMS",
            joinColumns = { @JoinColumn(name = "competition_id") },
            inverseJoinColumns = { @JoinColumn(name = "team_id") })
    private List<Team> teams;
}
