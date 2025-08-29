package com.svalero.tourfrance.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private String specialty;
    @Column
    private String birthplace;
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
    @JsonBackReference(value = "teams_cyclists")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Team team;
}
