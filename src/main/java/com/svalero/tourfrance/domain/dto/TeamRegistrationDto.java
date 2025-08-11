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
public class TeamRegistrationDto {
    //private long id;
    @NotNull(message = "El campo name es obligatorio")
    private String name;
    @NotNull(message = "El campo country es obligatorio")
    private String country;
    private String email;
    @Min(value = 0)
    private float budget;
    private LocalDate fundationDate;
}
