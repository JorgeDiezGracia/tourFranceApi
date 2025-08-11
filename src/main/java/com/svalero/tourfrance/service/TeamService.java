package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.*;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<TeamOutDto> getAll(String name, String country, String email) {
        List<Team> teamList;

        if (name.isEmpty() && country.isEmpty() && email.isEmpty()) {
            teamList = teamRepository.findAll();
        } else if (name.isEmpty() && !country.isEmpty() && email.isEmpty()) {
            teamList = teamRepository.findByCountry(country);
        } else if (!name.isEmpty() && country.isEmpty() && email.isEmpty()) {
            teamList = teamRepository.findByName(name);
        } else if (name.isEmpty() && country.isEmpty() && !email.isEmpty()) {
            teamList = teamRepository.findByEmail(email);
        } else if (!name.isEmpty() && !country.isEmpty() && email.isEmpty()) {
            teamList = teamRepository.findByNameAndCountry(name, country);
        } else if (!name.isEmpty() && country.isEmpty() && !email.isEmpty()) {
            teamList = teamRepository.findByNameAndEmail(name, email);
        } else if (name.isEmpty() && !country.isEmpty() && !email.isEmpty()) {
            teamList = teamRepository.findByCountryAndEmail(country, email);
        } else {
            teamList = teamRepository.findByNameAndCountryAndEmail(name, country, email);
        }

        List<TeamOutDto> teamOutDtos = modelMapper.map(teamList, new TypeToken<List<TeamOutDto>>() {}.getType());
        return teamOutDtos;

    }

    public Team get(long id) throws TeamNotFoundException {
        return teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
    }

    public TeamOutDto add(TeamRegistrationDto teamInDto) {
        Team team = modelMapper.map(teamInDto, Team.class);
        Team newTeam = teamRepository.save(team);
         return modelMapper.map(newTeam, TeamOutDto.class);
    }

    public TeamOutDto modify(long teamId, TeamInDto teamInDto) throws TeamNotFoundException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);


        modelMapper.map(teamInDto, team);
        modelMapper.typeMap(TeamInDto.class, Team.class)
                .addMappings(mapper -> mapper.skip(Team::setId));
        modelMapper.map(teamInDto, team);
        teamRepository.save(team);
        return modelMapper.map(team, TeamOutDto.class);
    }


    public void remove(long id) throws TeamNotFoundException{
        teamRepository.findById(id)
                .orElseThrow(TeamNotFoundException::new);
        teamRepository.deleteById(id);
    }
}
