package com.svalero.tourfrance.controller;

import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;

    @GetMapping("/teams")
    public List<Team> getAll() {
        return teamService.getAll();
    }

    @PostMapping("/teams")
    public void addTeam(Team team) {
        teamService.add(team);
    }

    @DeleteMapping("/teams/:teamId")
    public void removeTeam(long teamId) {
        teamService.remove(teamId);
    }
}
