package com.svalero.tourfrance.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cyclists")
public class Cyclist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private int titles;
    @Column
    private float weight;
    @Column
    private LocalDate birthdate;
    @Column
    private boolean isLeader;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
