package com.svalero.tourfrance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClimbOutDto {
    private long id;
    private String name;
    private String category;
    private String region;
    private int altitude;
    private float slope;
    private boolean goal;
    private LocalDate lastAscent;
    //private float longitude;
    //private float latitude;
    private long stageId;
}
