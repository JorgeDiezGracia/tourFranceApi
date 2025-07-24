package com.svalero.tourfrance.domain.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClimbInDto {
    //private long id;
    @NotNull(message = "El campo name es obligatorio")
    private String name;
    @NotNull(message = "El campo category es obligatorio")
    private String category;
    private String region;
    private int altitude;
    @Min(value = 0)
    private float slope;
    //private boolean goal;
    private LocalDate lastAscent;
    //private float longitude;
    //private float latitude;
    private long stageId;
}
