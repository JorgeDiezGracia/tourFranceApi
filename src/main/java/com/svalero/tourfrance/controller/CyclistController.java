package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.service.CyclistService;
import com.svalero.tourfrance.service.TeamService;
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
    public ResponseEntity<List<Cyclist>> getAll() {
        return new ResponseEntity<>(cyclistService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/cyclists/:cyclistId")
    public ResponseEntity<Cyclist> getTeam(long cyclistId) throws CyclistNotFoundException {
        Cyclist cyclist = cyclistService.get(cyclistId);
        return new ResponseEntity<>(cyclist, HttpStatus.OK);
    }

    @PostMapping("/cyclists")
    public ResponseEntity<Cyclist> addCyclist(@RequestBody Cyclist cyclist) {
        return new ResponseEntity<>(cyclistService.add(cyclist), HttpStatus.CREATED);
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

}
