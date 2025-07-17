package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.CyclistInDto;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.domain.dto.CyclistRegistrationDto;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.CyclistRepository;
import com.svalero.tourfrance.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CyclistService {

    @Autowired
    private CyclistRepository cyclistRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<CyclistOutDto> getAll(String name, String specialty, String birthplace) {
        List<Cyclist> cyclistList;

        if (name.isEmpty() && specialty.isEmpty() && birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findAll();
        } else if (name.isEmpty() && !specialty.isEmpty() && birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findBySpecialty(specialty);
        } else if (!name.isEmpty() && specialty.isEmpty() && birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findByName(name);
        } else if (name.isEmpty() && specialty.isEmpty() && !birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findByBirthplace(birthplace);
        } else if (!name.isEmpty() && !specialty.isEmpty() && birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findByNameAndSpecialty(name, specialty);
        } else if (!name.isEmpty() && specialty.isEmpty() && !birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findByNameAndBirthplace(name, birthplace);
        } else if (name.isEmpty() && !specialty.isEmpty() && !birthplace.isEmpty()) {
            cyclistList = cyclistRepository.findBySpecialtyAndBirthplace(specialty, birthplace);
        } else {
            cyclistList = cyclistRepository.findByNameAndSpecialtyAndBirthplace(name, specialty, birthplace);
        }


        List<CyclistOutDto> cyclistOutDtos = modelMapper.map(cyclistList, new TypeToken<List<CyclistOutDto>>() {}.getType());
        return cyclistOutDtos;
    }

    public Cyclist get(long id) throws CyclistNotFoundException {
        return cyclistRepository.findById(id)
                .orElseThrow(CyclistNotFoundException::new);
    }

    public CyclistOutDto add(long teamId, CyclistRegistrationDto cyclistInDto) throws TeamNotFoundException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        Cyclist cyclist = modelMapper.map(cyclistInDto, Cyclist.class);
        cyclist.setTeam(team);
        Cyclist newCyclist = cyclistRepository.save(cyclist);

        return modelMapper.map(newCyclist, CyclistOutDto.class);
    }

    public CyclistOutDto modify(long cyclistId, CyclistInDto cyclistInDto) throws CyclistNotFoundException, TeamNotFoundException {
       Cyclist cyclist = cyclistRepository.findById(cyclistId)
                .orElseThrow(CyclistNotFoundException::new);

        Team team = teamRepository.findById(cyclistInDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        cyclist.setTeam(team);

       //modelMapper.map(cyclistInDto, cyclist);

        modelMapper.typeMap(CyclistInDto.class, Cyclist.class)
                .addMappings(mapper -> mapper.skip(Cyclist::setId));
        modelMapper.map(cyclistInDto, cyclist);

        cyclistRepository.save(cyclist);

       return modelMapper.map(cyclist, CyclistOutDto.class);
    }

    public void remove(long id) throws CyclistNotFoundException {
        cyclistRepository.findById(id)
                .orElseThrow(CyclistNotFoundException::new);
        cyclistRepository.deleteById(id);
    }
}





