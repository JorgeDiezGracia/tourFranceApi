package com.svalero.tourfrance.domain.dto;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageOutDto {
    private long id;
    private String departure;
    private String arrival;
    private String type;
    private int elevation;
    private float kilometers;
    private boolean mountainStage;
    private LocalDate stageDate;
//    private double longitude;
//    private double latitude;

}
