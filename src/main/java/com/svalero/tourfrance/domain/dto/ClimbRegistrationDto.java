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
public class ClimbRegistrationDto {
    //private long id;
    @NotNull(message = "El campo name es obligatorio")
    private String name;
    @NotNull(message = "El campo category es obligatorio")
    private String category;
    private String region;
    @Min(value = 0)
    private int altitude;
    private float slope;
    private boolean goal;
    private LocalDate lastAscent;
    private float longitude;
    private float latitude;
}
