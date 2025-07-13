package com.svalero.tourfrance.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CyclistOutDto {
    private long id;
    private String name;
    private int titles;
    //private float weight;
    private LocalDate birthdate;
    //private boolean isLeader;
    private long teamId;
}
