package com.santex.models.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.santex.json.deserializers.OffsetDateTimeDeserializer;
import com.santex.json.serializers.OffsetDateTimeSerializer;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
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

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(name = "fk_PLAYERS_TEAMS"))
    private Team team;
}
