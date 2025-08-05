package com.svalero.tourfrance;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.domain.dto.CyclistRegistrationDto;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.CyclistRepository;
import com.svalero.tourfrance.repository.TeamRepository;
import com.svalero.tourfrance.service.CyclistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.LambdaUtil.getAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CyclistServiceTests {

    @InjectMocks
    private CyclistService cyclistService;

    @Mock
    private CyclistRepository cyclistRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TeamRepository teamRepository;

    @Test
    public void testGetAll() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null),
                new Cyclist(2, "Vingegaard", "escalador", "Dinamarca", 87, 58, LocalDate.now(), true, null),
                new Cyclist(2, "Romeo", "escalador", "España", 104, 3, LocalDate.now(), false, null)
        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(2, "Vingegaard", "escalador", "Dinamarca", 87, LocalDate.now(), 65),
                new CyclistOutDto(2, "Romeo", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistRepository.findAll()).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("", "", "");
        assertEquals(3, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("Romeo", cyclistList.getLast().getName());

        //añadir todas los métodos a los que podría llamar
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(0)).findByName("");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findBySpecialty("");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("", "");
        verify(cyclistRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllByName() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findByName("Pogaçar")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("Pogaçar", "", "");
        assertEquals(1, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("escalador", cyclistList.getFirst().getSpecialty());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(0)).findBySpecialty("escalador");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("Pogaçar", "");
        verify(cyclistRepository, times(1)).findByName("Pogaçar");
    }

    @Test
    public void testGetAllBySpecialty() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findBySpecialty("escalador")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("", "escalador", "");
        assertEquals(1, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("escalador", cyclistList.getFirst().getSpecialty());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(1)).findBySpecialty("escalador");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("Pogaçar", "");
        verify(cyclistRepository, times(0)).findByName("Pogaçar");
    }

    @Test
    public void testGetAllByBirthplace() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findByBirthplace("Eslovenia")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("", "", "Eslovenia");
        assertEquals(1, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("escalador", cyclistList.getFirst().getSpecialty());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(1)).findByBirthplace("Eslovenia");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(0)).findBySpecialty("escalador");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("Pogaçar", "");
        verify(cyclistRepository, times(0)).findByName("Pogaçar");
    }

    @Test
    public void testGetAllByNameAndSpecialty() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findByNameAndSpecialty("Pogaçar", "escalador")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("Pogaçar", "escalador", "");
        assertEquals(1, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("escalador", cyclistList.getFirst().getSpecialty());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(0)).findBySpecialty("escalador");
        verify(cyclistRepository, times(1)).findByNameAndSpecialty("Pogaçar", "escalador");
        verify(cyclistRepository, times(0)).findByName("Pogaçar");
    }

    @Test
    public void testGetAllByNameAndBirthplace() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findByNameAndBirthplace("Pogaçar", "Eslovenia")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("Pogaçar", "", "Eslovenia");
        assertEquals(1, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("Eslovenia", cyclistList.getFirst().getBirthplace());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(1)).findByNameAndBirthplace("Pogaçar", "Eslovenia");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(0)).findBySpecialty("escalador");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("Pogaçar", "escalador");
        verify(cyclistRepository, times(0)).findByName("Pogaçar");
    }

    @Test
    public void testGetAllBySpecialtyAndBirthplace() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findBySpecialtyAndBirthplace("escalador", "Eslovenia")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("", "escalador", "Eslovenia");
        assertEquals(1, cyclistList.size());
        assertEquals("escalador", cyclistList.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistList.getFirst().getBirthplace());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(0)).findByNameAndSpecialtyAndBirthplace("", "", "");
        verify(cyclistRepository, times(1)).findBySpecialtyAndBirthplace("escalador", "Eslovenia");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(0)).findBySpecialty("escalador");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("Pogaçar", "escalador");
        verify(cyclistRepository, times(0)).findByName("Pogaçar");
    }

    @Test
    public void testGetAllByNameAndSpecialtyAndBirthplace() {
        List<Cyclist> mockCyclistList = List.of(
                new Cyclist(1, "Pogaçar", "escalador", "Eslovenia", 104, 58, LocalDate.now(), true, null)

        );
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98)

        );

        when(cyclistRepository.findByNameAndSpecialtyAndBirthplace("Pogaçar", "escalador", "Eslovenia")).thenReturn(mockCyclistList);
        when(modelMapper.map(mockCyclistList, new TypeToken<List<CyclistOutDto>>() {
        }.getType())).thenReturn(mockCyclistDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<CyclistOutDto> cyclistList = cyclistService.getAll("Pogaçar", "escalador", "Eslovenia");
        assertEquals(1, cyclistList.size());
        assertEquals("Pogaçar", cyclistList.getFirst().getName());
        assertEquals("escalador", cyclistList.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistList.getFirst().getBirthplace());

        //mirar todas las opciones según el if
        verify(cyclistRepository, times(1)).findByNameAndSpecialtyAndBirthplace("Pogaçar", "escalador", "Eslovenia");
        verify(cyclistRepository, times(0)).findBySpecialtyAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByNameAndBirthplace("", "");
        verify(cyclistRepository, times(0)).findByBirthplace("");
        verify(cyclistRepository, times(0)).findAll();
        verify(cyclistRepository, times(0)).findBySpecialty("escalador");
        verify(cyclistRepository, times(0)).findByNameAndSpecialty("Pogaçar", "escalador");
        verify(cyclistRepository, times(0)).findByName("Pogaçar");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testAdd() {
        long teamId = 1;
        Team mockTeam = new Team(teamId, "UAE", "uae@uae.com", "Emirates", 150, LocalDate.now(), new ArrayList<>());
        CyclistRegistrationDto cyclistRegistrationDto = new CyclistRegistrationDto("Pogaçar", "escalador", "Eslovenia", 101, 64, LocalDate.now(), true, 98);

        Cyclist mockCyclist = new Cyclist(0, "Pogaçar", "escalador", "Eslovenia", 101, 68, LocalDate.now(), true, null);
        CyclistOutDto mockCyclistOutDto = new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 101, LocalDate.now(), 98);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        when(modelMapper.map(cyclistRegistrationDto, Cyclist.class)).thenReturn(mockCyclist);
        when(modelMapper.map(mockCyclist, CyclistOutDto.class)).thenReturn(mockCyclistOutDto);

        try {
            cyclistService.add(teamId, cyclistRegistrationDto);
        } catch (TeamNotFoundException unfe) {

        }
        assertEquals(1, mockCyclistOutDto.getId());
        assertEquals("Pogaçar", mockCyclistOutDto.getName());
        assertEquals("escalador", mockCyclistOutDto.getSpecialty());
        assertEquals("Eslovenia", mockCyclistOutDto.getBirthplace());
        assertEquals(101, mockCyclistOutDto.getTitles());
        assertEquals(LocalDate.now(), mockCyclistOutDto.getBirthdate());
        assertEquals(98, mockCyclistOutDto.getTeamId());

        verify(cyclistRepository, times(1)).save(mockCyclist);
        //verify(modelMapper, times(1)).map(cyclistRegistrationDto, Cyclist.class);
        //verify(modelMapper, times(1)).map(mockCyclist, CyclistOutDto.class);
    }

    @Test
    public void testAddTeamNotFound() throws TeamNotFoundException {
        long teamId = 150;
        CyclistRegistrationDto cyclistRegistrationDto = new CyclistRegistrationDto("Pogaçar", "escalador", "Eslovenia", 101, 64, LocalDate.now(), true, 98);

        try {
            when(teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new)).thenThrow(TeamNotFoundException.class);
        } catch (TeamNotFoundException unfe) {
        }
        assertThrows(TeamNotFoundException.class, () -> cyclistService.add(teamId, cyclistRegistrationDto));
    }
}
