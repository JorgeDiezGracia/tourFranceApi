package com.svalero.tourfrance.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StageRegistrationDto {
    //private long id;
    @NotNull(message = "El campo departure es obligatorio")
    private String departure;
    @NotNull(message = "El campo arrival es obligatorio")
    private String arrival;
    private String type;
    private int elevation;
    @Min(value = 0)
    private float kilometers;
    private boolean mountainStage;
    private LocalDate stageDate;
//    private double longitude;
//    private double latitude;
}
