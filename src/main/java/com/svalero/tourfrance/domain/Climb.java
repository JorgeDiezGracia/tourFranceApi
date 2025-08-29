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
@Entity(name = "climbs")
public class Climb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String name;
    @Column
    private String category;
    @Column
    private String region;
    @Column
    private int altitude;
    @Column
    private float slope;
    @Column
    private boolean goal;
    @Column
    private LocalDate lastAscent;
    @Column
    private float longitude;
    @Column
    private float latitude;

    @ManyToOne
    @JoinColumn(name = "stage_id")
    @JsonBackReference(value = "stages_climbs")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Stage stage;
}
