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
@Table(name = "COACHES")
public class Coach {

    @Id
    @Column(name = "coach_name")
    String coachName;

    @Column(name = "coach_country")
    String coachCountry;

    @Column(name = "coach_age")
    String coachAge;
}
