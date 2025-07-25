package com.svalero.tourfrance.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="sponsors")
public class Sponsor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String country;
    @Column
    private String email;
    @Column
    private int employees;
    @Column
    private float funding;
    @Column(name="end_contract")
    private LocalDate endContract;
    @Column
    private boolean mainSponsor;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonManagedReference(value = "teams_sponsors")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Team team;

    //@OneToMany(mappedBy = "team")
    //@JsonBackReference(value = "teams_sponsors")
    //private List<Sponsor> sponsors;
}
