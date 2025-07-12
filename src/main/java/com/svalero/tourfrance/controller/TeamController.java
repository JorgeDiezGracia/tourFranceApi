package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAll() {
        return new ResponseEntity<>(teamService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/teams/:teamId")
    public ResponseEntity<Team> getTeam(long teamId) throws TeamNotFoundException {
        Team team = teamService.get(teamId);
        return new ResponseEntity<>(team, HttpStatus.OK);
    }

    @PostMapping("/teams")
    public ResponseEntity<Team> addTeam(@RequestBody Team team) {
        return new ResponseEntity<>(teamService.add(team), HttpStatus.CREATED);
    }

    @DeleteMapping("/teams/:teamId")
    public ResponseEntity<Void> removeTeam(long teamId) throws TeamNotFoundException{
        teamService.remove(teamId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    public ResponseEntity<Void> handleTeamNotFoundException(TeamNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
