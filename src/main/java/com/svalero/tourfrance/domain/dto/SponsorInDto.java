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
public class SponsorInDto {
    //private long id;
    @NotNull(message = "El campo name es obligatorio")
    private String name;
    @NotNull(message = "El campo country es obligatorio")
    private String country;
    private String email;
    private int employees;
    @Min(value = 0)
    private float funding;
    //private boolean mainSponsor;
    private LocalDate endContract;
    private long teamId;
}
