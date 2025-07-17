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
public class CyclistInDto {
    //private long id;
    @NotNull(message = "El campo name es obligatorio")
    private String name;
    @NotNull(message = "El campo specialty es obligatorio")
    private String specialty;
    private String birthplace;
    @Min(value = 0)
    private int titles;
    //private float weight;
    @NotNull
    private LocalDate birthdate;
    //private boolean isLeader;
    private long teamId;
}


