package com.svalero.tourfrance.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="teams")

public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String email;
    @Column
    private String country;
    @Column
    private float budget;
    @Column(name="fundation_date")
    private LocalDate fundationDate;

    @OneToMany(mappedBy = "team")
    @JsonBackReference(value = "teams_cyclists")
    private List<Cyclist> cyclists;
}
