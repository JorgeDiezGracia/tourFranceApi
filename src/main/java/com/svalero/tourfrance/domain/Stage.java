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
@Entity(name = "stage")
public class Stage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String departure;
    @Column
    private String arrival;
    @Column
    private String type;
    @Column
    private int elevation;
    @Column
    private float kilometers;
    @Column
    private boolean mountainStage;
    @Column(name="stage_date")
    private LocalDate stageDate;
//    @Column
//    private double longitude;
//    @Column
//    private double latitude;
    @OneToMany(mappedBy = "stage")
    @JsonBackReference(value = "stages_climbs")
    private List<Climb> climbs;


}
