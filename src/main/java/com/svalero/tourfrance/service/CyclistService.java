package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.CyclistInDto;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
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

    public List<CyclistOutDto> getAll() {
        List<Cyclist> allCyclists = cyclistRepository.findAll();

        List<CyclistOutDto> cyclistOutDtos = modelMapper.map(allCyclists, new TypeToken<List<CyclistOutDto>>() {}.getType());
        return cyclistOutDtos;
    }

    public Cyclist get(long id) throws CyclistNotFoundException {
        return cyclistRepository.findById(id)
                .orElseThrow(CyclistNotFoundException::new);
    }

    public CyclistOutDto add(long teamId, CyclistInDto cyclistInDto) throws TeamNotFoundException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        Cyclist cyclist = modelMapper.map(cyclistInDto, Cyclist.class);
        cyclist.setTeam(team);
        Cyclist newCyclist = cyclistRepository.save(cyclist);
        return modelMapper.map(cyclist, CyclistOutDto.class);
    }

    public void remove(long id) throws CyclistNotFoundException {
        cyclistRepository.findById(id)
                .orElseThrow(CyclistNotFoundException::new);
        cyclistRepository.deleteById(id);
    }
}





