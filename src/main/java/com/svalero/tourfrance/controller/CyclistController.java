package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.dto.CyclistInDto;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.service.CyclistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CyclistController {

    @Autowired
    private CyclistService cyclistService;

    @GetMapping("/cyclists")
    public ResponseEntity<List<CyclistOutDto>> getAll() {
        return new ResponseEntity<>(cyclistService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/cyclists/:cyclistId")
    public ResponseEntity<Cyclist> getCyclist(long cyclistId) throws CyclistNotFoundException {
        Cyclist cyclist = cyclistService.get(cyclistId);
        return new ResponseEntity<>(cyclist, HttpStatus.OK);
    }

    @PostMapping("/teams/:teamId/cyclists")
    public ResponseEntity<CyclistOutDto> addCyclist(long teamId, @Valid @RequestBody CyclistInDto cyclist) throws TeamNotFoundException {
        CyclistOutDto newCyclist = cyclistService.add(teamId, cyclist);
        return new ResponseEntity<>(newCyclist, HttpStatus.CREATED);
    }

    @DeleteMapping("/cyclists/:cyclistId")
    public ResponseEntity<Void> removeCyclist(long cyclistId) throws CyclistNotFoundException{
        cyclistService.remove(cyclistId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleCyclistNotFoundException(CyclistNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleTeamNotFoundException(TeamNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
