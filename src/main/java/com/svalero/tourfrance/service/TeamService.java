package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;


    public List<Team> getAll() {
        List<Team> allTeams = teamRepository.findAll();
        return allTeams;
    }

    public void add(Team team) {
         teamRepository.save(team);
    }

    public void remove(long id) {
        teamRepository.deleteById(id);

    }

}
