package com.svalero.tourfrance.service;

import com.svalero.tourfrance.domain.Sponsor;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.domain.dto.SponsorInDto;
import com.svalero.tourfrance.domain.dto.SponsorOutDto;
import com.svalero.tourfrance.domain.dto.SponsorRegistrationDto;
import com.svalero.tourfrance.exception.SponsorNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.SponsorRepository;
import com.svalero.tourfrance.repository.TeamRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SponsorService {


    @Autowired
    private SponsorRepository sponsorRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<SponsorOutDto> getAll(String name, String country, String email) {
        List<Sponsor> sponsorList;

        if (name.isEmpty() && country.isEmpty() && email.isEmpty()) {
            sponsorList = sponsorRepository.findAll();
        } else if (name.isEmpty() && !country.isEmpty() && email.isEmpty()) {
            sponsorList = sponsorRepository.findByCountry(country);
        } else if (!name.isEmpty() && country.isEmpty() && email.isEmpty()) {
            sponsorList = sponsorRepository.findByName(name);
        } else if (name.isEmpty() && country.isEmpty() && !email.isEmpty()) {
            sponsorList = sponsorRepository.findByEmail(email);
        } else if (!name.isEmpty() && !country.isEmpty() && email.isEmpty()) {
            sponsorList = sponsorRepository.findByNameAndCountry(name, country);
        } else if (!name.isEmpty() && country.isEmpty() && !email.isEmpty()) {
            sponsorList = sponsorRepository.findByNameAndEmail(name, email);
        } else if (name.isEmpty() && !country.isEmpty() && !email.isEmpty()) {
            sponsorList = sponsorRepository.findByCountryAndEmail(country, email);
        } else {
            sponsorList = sponsorRepository.findByNameAndCountryAndEmail(name, country, email);
        }


        List<SponsorOutDto> sponsorOutDtos = modelMapper.map(sponsorList, new TypeToken<List<SponsorOutDto>>() {}.getType());
        return sponsorOutDtos;
    }

    public Sponsor get(long id) throws SponsorNotFoundException {
        return sponsorRepository.findById(id)
                .orElseThrow(SponsorNotFoundException::new);
    }

    public SponsorOutDto add(long teamId, SponsorRegistrationDto sponsorInDto) throws TeamNotFoundException {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);


        Sponsor sponsor = modelMapper.map(sponsorInDto, Sponsor.class);
        sponsor.setTeam(team);
        Sponsor newSponsor =sponsorRepository.save(sponsor);

        return modelMapper.map(newSponsor, SponsorOutDto.class);
    }

    public SponsorOutDto modify(long sponsorId, SponsorInDto sponsorInDto) throws SponsorNotFoundException, TeamNotFoundException {
        Sponsor sponsor = sponsorRepository.findById(sponsorId)
                .orElseThrow(SponsorNotFoundException::new);

        Team team = teamRepository.findById(sponsorInDto.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        sponsor.setTeam(team);

        modelMapper.typeMap(SponsorInDto.class, Sponsor.class)
                .addMappings(mapper -> mapper.skip(Sponsor::setId));
        modelMapper.map(sponsorInDto, sponsor);

        sponsorRepository.save(sponsor);
        return modelMapper.map(sponsor, SponsorOutDto.class);
    }

    public void remove(long id) throws SponsorNotFoundException {
        sponsorRepository.findById(id)
                .orElseThrow(SponsorNotFoundException::new);
        sponsorRepository.deleteById(id);
    }

}
