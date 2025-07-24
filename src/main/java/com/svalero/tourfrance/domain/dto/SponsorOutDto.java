package com.svalero.tourfrance.domain.dto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SponsorOutDto {
    private long id;
    private String name;
    private String country;
    private String email;
    private int employees;
    //private float funding;
    private LocalDate endContract;
    //private boolean mainSponsor;
    private long teamId;
}
