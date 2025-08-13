package com.svalero.tourfrance;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.CyclistInDto;
import com.svalero.tourfrance.domain.dto.TeamInDto;
import com.svalero.tourfrance.domain.dto.TeamOutDto;
import com.svalero.tourfrance.domain.dto.TeamRegistrationDto;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.TeamRepository;
import com.svalero.tourfrance.repository.CyclistRepository;
import com.svalero.tourfrance.service.TeamService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTests {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CyclistRepository ciclistRepository;

    @Test
    public void testGetAll(){
        List<Team> mockTeamList = List.of(
                new Team(1, "Visma", "visma@visma.com", "Dinamarca", 300, LocalDate.now(), new ArrayList<>()),
                new Team(2, "UAE", "uae@uae.com", "Emiratos Arabes", 450, LocalDate.now(), new ArrayList<>()),
                new Team(3, "Movistar", "movistar@movistar.com", "España", 100, LocalDate.now(), new ArrayList<>())

        );
        List<TeamOutDto> mockTeamDtoList = List.of(
                new TeamOutDto(1, "Visma", "visma@visma.com" ,"Dinamarca", 300, LocalDate.now()),
                new TeamOutDto(1, "UAE", "uae@uae.com" ,"Emiratos Arabes", 450, LocalDate.now()),
                new TeamOutDto(1, "Movistar", "movistar@movistar.com" ,"España", 100, LocalDate.now())
        );

        when(teamRepository.findAll()).thenReturn(mockTeamList);
        when(modelMapper.map(mockTeamList, new TypeToken<List<TeamOutDto>>() {
        }.getType())).thenReturn(mockTeamDtoList);

        //listado de equipos sin filtros

        List<TeamOutDto> teamList = teamService.getAll("", "", "");
        assertEquals(3, teamList.size());
        assertEquals("Visma", teamList.getFirst().getName());
        assertEquals("Movistar", teamList.getLast().getName());

        //métodos a los que podría llamar
        verify(teamRepository, times(1)).findAll();
        verify(teamRepository, times(0)).findByName("");
        verify(teamRepository, times(0)).findByCountry("");
        verify(teamRepository, times(0)).findByEmail("");
        verify(teamRepository, times(0)).findByNameAndCountry("","");
        verify(teamRepository, times(0)).findByNameAndEmail("", "");
        verify(teamRepository, times(0)).findByCountryAndEmail("","");
        verify(teamRepository, times(0)).findByNameAndCountryAndEmail("","","");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testAdd(){
        TeamRegistrationDto teamRegistrationDto = new TeamRegistrationDto("Visma", "visma@visma.com", "Dinamarca", 300, LocalDate.now());
        Team mockTeam = new Team(1,"Visma", "visma@visma.com", "Dinamarca", 300, LocalDate.now(), null);
        TeamOutDto mockTeamOutDto = new TeamOutDto(1, "Visma", "Dinamarca", "visma@visma.com", 300, LocalDate.now());

        when(modelMapper.map(teamRegistrationDto, Team.class)).thenReturn(mockTeam);
        when(teamRepository.save(mockTeam)).thenReturn(mockTeam);
        when(modelMapper.map(mockTeam, TeamOutDto.class)).thenReturn(mockTeamOutDto);

        TeamOutDto result = teamService.add(teamRegistrationDto);

        assertEquals(1, result.getId());
        assertEquals("Visma", result.getName());
        assertEquals("Dinamarca", result.getCountry());
        assertEquals("visma@visma.com", result.getEmail());
        assertEquals(300, result.getBudget());
        assertEquals(LocalDate.now(), result.getFundationDate());


        verify(teamRepository, times(1)).save(mockTeam);
    }

    @Test
    public void testModifyTeam() throws TeamNotFoundException {
        // Arrange
        long teamId = 1;

        TeamInDto teamInDto = new TeamInDto();
        teamInDto.setName("Updated Team");
        teamInDto.setEmail("updated@team.com");
        teamInDto.setCountry("Updated Country");
        teamInDto.setBudget(500);

        Team existingTeam = new Team();
        existingTeam.setId(teamId);
        existingTeam.setName("Old Team");
        existingTeam.setEmail("old@team.com");
        existingTeam.setCountry("Old Country");
        existingTeam.setBudget(300);

        TeamOutDto expectedOutDto = new TeamOutDto();
        expectedOutDto.setId(teamId);
        expectedOutDto.setName("Updated Team");
        expectedOutDto.setEmail("updated@team.com");
        expectedOutDto.setCountry("Updated Country");
        expectedOutDto.setBudget(500);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));

        @SuppressWarnings("unchecked")
        TypeMap<TeamInDto, Team> mockTypeMap = mock(TypeMap.class);
        when(modelMapper.typeMap(TeamInDto.class, Team.class)).thenReturn(mockTypeMap);

        // Aquí mockeamos addMappings para que devuelva el mismo mock (encadenamiento)
        when(mockTypeMap.addMappings(
                ArgumentMatchers.<org.modelmapper.ExpressionMap<TeamInDto, Team>>any()
        )).thenReturn(mockTypeMap);

        // Mockear mapping que modifica el objeto destino
        doAnswer(invocation -> {
            TeamInDto source = invocation.getArgument(0);
            Team target = invocation.getArgument(1);
            target.setName(source.getName());
            target.setEmail(source.getEmail());
            target.setCountry(source.getCountry());
            target.setBudget(source.getBudget());
            return null;
        }).when(modelMapper).map(eq(teamInDto), eq(existingTeam));

        when(modelMapper.map(existingTeam, TeamOutDto.class)).thenReturn(expectedOutDto);

        when(teamRepository.save(existingTeam)).thenReturn(existingTeam);

        // Act
        TeamOutDto result = teamService.modify(teamId, teamInDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOutDto.getId(), result.getId());
        assertEquals(expectedOutDto.getName(), result.getName());
        assertEquals(expectedOutDto.getEmail(), result.getEmail());
        assertEquals(expectedOutDto.getCountry(), result.getCountry());
        assertEquals(expectedOutDto.getBudget(), result.getBudget());

        verify(teamRepository).findById(teamId);
        verify(modelMapper, times(2)).map(teamInDto, existingTeam);
        verify(teamRepository).save(existingTeam);
        verify(modelMapper).map(existingTeam, TeamOutDto.class);
    }

    @Test
    public void testModifyTeamNotFound() throws TeamNotFoundException {
        long teamId = 1;
        TeamInDto teamInDto = new TeamInDto();

        try {
            when(teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new))
                    .thenThrow(TeamNotFoundException.class);
        } catch (TeamNotFoundException e) {

        }

        assertThrows(TeamNotFoundException.class, () -> teamService.modify(teamId, teamInDto));
    }

    @Test
    public void testRemoveTeam() throws TeamNotFoundException {
        long teamId = 1;

        Team team = new Team();
        team.setId(teamId);

        // Simula que el ciclista existe
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // Act
        teamService.remove(teamId);

        // Assert
        verify(teamRepository).findById(teamId);
        verify(teamRepository).deleteById(teamId);
    }

}
