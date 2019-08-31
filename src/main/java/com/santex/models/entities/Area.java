package com.santex.models.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Area {

    @Column(name = "area_name")
    private String name;
}
