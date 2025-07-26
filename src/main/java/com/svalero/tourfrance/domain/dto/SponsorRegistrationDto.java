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
public class SponsorRegistrationDto {

    //private long id;
    @NotNull(message = "El campo name es obligatorio")
    private String name;
    @NotNull(message = "El campo country es obligatorio")
    private String country;
    private String email;
    @Min(value = 0)
    private int employees;
    private float funding;
    private LocalDate endContract;
    private boolean mainSponsor;
    private long teamId;
}
