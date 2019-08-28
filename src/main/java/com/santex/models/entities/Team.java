package com.santex.models.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "TEAMS")
public class Team implements Serializable {

    @Id
    private Long id;
    private String name;
    private String tla;
    private Area area;

    @Column(name = "short_name")
    private String shortName;

    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "teams")
    private List<Competition> competitions;

    @JsonAlias({"squad"})
    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Player> players;
}