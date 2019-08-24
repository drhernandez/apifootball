package com.santex.sarasa;

import com.santex.models.Competition;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
//@Entity
//@Table(name = "COUNTRIES")
public class Country {

    @Id
    @Column(name = "country_id")
    private String countryId;

    @Column(name = "country_name")
    private String countryName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "league_id", foreignKey = @ForeignKey(name = "fk_COUNTRIES_COMPETITIONS"))
    private List<Competition> competitions;
}
