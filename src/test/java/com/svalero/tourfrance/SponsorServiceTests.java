package com.svalero.tourfrance;

import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Sponsor;
import com.svalero.tourfrance.domain.Team;

import com.svalero.tourfrance.domain.dto.SponsorInDto;
import com.svalero.tourfrance.domain.dto.SponsorOutDto;

import com.svalero.tourfrance.domain.dto.SponsorRegistrationDto;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.SponsorNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.SponsorRepository;
import com.svalero.tourfrance.repository.TeamRepository;
import com.svalero.tourfrance.service.SponsorService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SponsorServiceTests {

    @InjectMocks
    private SponsorService sponsorService;

    @Mock
    private SponsorRepository sponsorRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private TeamRepository teamRepository;

    @Test
    public void testGetSponsorByIdFound() throws SponsorNotFoundException {

        long sponsorId = 1L;
        Sponsor mockSponsor = new Sponsor();
        mockSponsor.setId(sponsorId);
        mockSponsor.setName("Decathlon");

        when(sponsorRepository.findById(sponsorId)).thenReturn(Optional.of(mockSponsor));

        Sponsor result = sponsorService.get(sponsorId);

        assertNotNull(result);
        assertEquals(sponsorId, result.getId());
        assertEquals("Decathlon", result.getName());
    }

    @Test
    public void testGetSponsorByIdNotFound() {
        long sponsorId = 99L;
        when(sponsorRepository.findById(sponsorId)).thenReturn(Optional.empty());

        assertThrows(SponsorNotFoundException.class, () -> {
            sponsorService.get(sponsorId);
        });
    }


    @Test
    public void testGetAll() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uae@uae.com", 1500, 2004, LocalDate.now(), true, null),
                new Sponsor(2, "Jumbo", "Dinamarca", "jumbo@jumbo.com", 540, 1990, LocalDate.now(), true, null),
                new Sponsor(3, "Movistar", "España", "movistar@movistar.es", 3500, 1960, LocalDate.now(), true, null)
        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uae@uae.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Jumbo", "Dinamarca", "jumbo@jumbo.com", 540, LocalDate.now(), 65),
                new SponsorOutDto(3, "Movistar", "España", "movistar@movistar.es", 3500, LocalDate.now(), 1)
        );

        when(sponsorRepository.findAll()).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("", "", "");
        assertEquals(3, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Movistar", sponsorList.getLast().getName());

        //añadir todas los métodos a los que podría llamar
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("", "");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllByName() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByName("UAD")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("UAD", "", "");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(1)).findByName("UAD");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("", "");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(0)).findAll();
    }

    @Test

    public void testGetAllByCountry() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByCountry("Emiratos Arabes")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("", "Emiratos Arabes", "");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("", "");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(1)).findByCountry("Emiratos Arabes");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(0)).findAll();
    }

    @Test
    public void testGetAllByEmail() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByEmail("uad@uad.com")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de sponsors sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("", "", "uad@uad.com");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("", "");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(1)).findByEmail("uad@uad.com");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(0)).findAll();
    }

    @Test
    public void testGetAllByNameAndCountry() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByNameAndCountry("UAD", "Emiratos Arabes")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de sponsors sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("UAD", "Emiratos Arabes", "");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("", "");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(1)).findByNameAndCountry("UAD", "Emiratos Arabes");
        verify(sponsorRepository, times(0)).findAll();
    }

    @Test
    public void testGetAllByNameAndEmail() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByNameAndEmail("UAD", "uad@uad.com")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de sponsors sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("UAD", "", "uad@uad.com");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("", "");
        verify(sponsorRepository, times(1)).findByNameAndEmail("UAD", "uad@uad.com");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(0)).findAll();
    }

    @Test
    public void testGetAllByCountryAndEmail() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByCountryAndEmail("Emiratos Arabes", "uad@uad.com")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de sponsors sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("", "Emiratos Arabes", "uad@uad.com");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(0)).findByNameAndCountryAndEmail("", "", "");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(1)).findByCountryAndEmail("Emiratos Arabes", "uad@uad.com");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(0)).findAll();
    }

    @Test
    public void testGetAllByNameAndCountryAndEmail() {
        List<Sponsor> mockSponsorList = List.of(
                new Sponsor(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null)

        );
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98)

        );

        when(sponsorRepository.findByNameAndCountryAndEmail("UAD","Emiratos Arabes", "uad@uad.com")).thenReturn(mockSponsorList);
        when(modelMapper.map(mockSponsorList, new TypeToken<List<SponsorOutDto>>() {
        }.getType())).thenReturn(mockSponsorDtoList);

        // listado de sponsors sin aplicar ningún filtro
        List<SponsorOutDto> sponsorList = sponsorService.getAll("UAD", "Emiratos Arabes", "uad@uad.com");
        assertEquals(1, sponsorList.size());
        assertEquals("UAD", sponsorList.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorList.getFirst().getCountry());

        //mirar todas las opciones según el if
        verify(sponsorRepository, times(1)).findByNameAndCountryAndEmail("UAD", "Emiratos Arabes", "uad@uad.com");
        verify(sponsorRepository, times(0)).findByName("");
        verify(sponsorRepository, times(0)).findByCountryAndEmail("Emiratos Arabes", "uad@uad.com");
        verify(sponsorRepository, times(0)).findByNameAndEmail("", "");
        verify(sponsorRepository, times(0)).findByEmail("");
        verify(sponsorRepository, times(0)).findByCountry("");
        verify(sponsorRepository, times(0)).findByNameAndCountry("", "");
        verify(sponsorRepository, times(0)).findAll();
    }


    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testAdd() {
        long teamId = 1;
        Team mockTeam = new Team(teamId, "UAE", "uae@uae.com", "Emirates", 150, LocalDate.now(), new ArrayList<>());
        SponsorRegistrationDto sponsorRegistrationDto = new SponsorRegistrationDto("UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, 98);

        Sponsor mockSponsor = new Sponsor(0, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null);
        SponsorOutDto mockSponsorOutDto = new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98);

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(mockTeam));
        when(modelMapper.map(sponsorRegistrationDto, Sponsor.class)).thenReturn(mockSponsor);
        when(modelMapper.map(mockSponsor, SponsorOutDto.class)).thenReturn(mockSponsorOutDto);

        try {
            sponsorService.add(teamId, sponsorRegistrationDto);
        } catch (TeamNotFoundException unfe) {

        }
        assertEquals(1, mockSponsorOutDto.getId());
        assertEquals("UAD", mockSponsorOutDto.getName());
        assertEquals("Emiratos Arabes", mockSponsorOutDto.getCountry());
        assertEquals("uad@uad.com", mockSponsorOutDto.getEmail());
        assertEquals(1500, mockSponsorOutDto.getEmployees());
        assertEquals(LocalDate.now(), mockSponsorOutDto.getEndContract());
        assertEquals(98, mockSponsorOutDto.getTeamId());

        verify(sponsorRepository, times(1)).save(mockSponsor);
        //verify(modelMapper, times(1)).map(cyclistRegistrationDto, Cyclist.class);
        //verify(modelMapper, times(1)).map(mockCyclist, CyclistOutDto.class);
    }

    @Test
    void testModify() throws SponsorNotFoundException, TeamNotFoundException {
        // Arrange
        long sponsorId = 1;
        long teamId = 100;

        SponsorInDto sponsorInDto = new SponsorInDto();
        sponsorInDto.setTeamId(teamId);
        sponsorInDto.setName("Updated Name");

        Sponsor existingSponsor = new Sponsor();
        existingSponsor.setId(sponsorId);

        Team team = new Team();
        team.setId(teamId);

        Sponsor updatedSponsor = new Sponsor(); // after mapping
        updatedSponsor.setId(sponsorId);
        updatedSponsor.setTeam(team);
        updatedSponsor.setName("Updated Name");

        SponsorOutDto expectedOutDto = new SponsorOutDto();
        expectedOutDto.setId(sponsorId);
        expectedOutDto.setName("Updated Name");

        // Mocking behavior
        when(sponsorRepository.findById(sponsorId)).thenReturn(Optional.of(existingSponsor));
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // ✅ Mock de TypeMap para evitar NullPointer
        TypeMap<SponsorInDto, Sponsor> mockTypeMap = mock(TypeMap.class);
        when(modelMapper.typeMap(SponsorInDto.class, Sponsor.class)).thenReturn(mockTypeMap);


        // Mapping input DTO to entity
        doAnswer(invocation -> {
            SponsorInDto source = invocation.getArgument(0);
            Sponsor target = invocation.getArgument(1);
            target.setName(source.getName()); // simulate mapping
            return null;
        }).when(modelMapper).map(eq(sponsorInDto), eq(existingSponsor));

        // Output DTO mapping
        when(modelMapper.map(existingSponsor, SponsorOutDto.class)).thenReturn(expectedOutDto);

        // Act
        SponsorOutDto result = sponsorService.modify(sponsorId, sponsorInDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOutDto.getId(), result.getId());
        assertEquals(expectedOutDto.getName(), result.getName());

        verify(sponsorRepository).findById(sponsorId);
        verify(teamRepository).findById(teamId);
        verify(modelMapper).map(sponsorInDto, existingSponsor);
        verify(sponsorRepository).save(existingSponsor);
        verify(modelMapper).map(existingSponsor, SponsorOutDto.class);
    }

    @Test
    public void testAddTeamNotFound() throws TeamNotFoundException {
        long teamId = 150;
        SponsorRegistrationDto sponsorRegistrationDto = new SponsorRegistrationDto("UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, 98);

        try {
            when(teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new)).thenThrow(TeamNotFoundException.class);
        } catch (TeamNotFoundException unfe) {
        }
        assertThrows(TeamNotFoundException.class, () -> sponsorService.add(teamId, sponsorRegistrationDto));
    }

    @Test
    public void testModifySponsorNotFound() throws SponsorNotFoundException, TeamNotFoundException {
        long sponsorId = 1;
        SponsorInDto sponsorInDto = new SponsorInDto();
        sponsorInDto.setTeamId(100);

        try {
            when(sponsorRepository.findById(sponsorId).orElseThrow(SponsorNotFoundException::new))
                    .thenThrow(SponsorNotFoundException.class);
        } catch (SponsorNotFoundException e) {

        }

        assertThrows(SponsorNotFoundException.class, () -> sponsorService.modify(sponsorId, sponsorInDto));
    }

    @Test
    public void testModifyTeamNotFound() throws SponsorNotFoundException, TeamNotFoundException {
        long sponsorId = 1;
        long teamId = 200;

        SponsorInDto sponsorInDto = new SponsorInDto();
        sponsorInDto.setTeamId(teamId);

        Sponsor existingSponsor = new Sponsor();
        existingSponsor.setId(sponsorId);

        when(sponsorRepository.findById(sponsorId)).thenReturn(Optional.of(existingSponsor));

        try {
            when(teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new))
                    .thenThrow(TeamNotFoundException.class);
        } catch (TeamNotFoundException e) {

        }

        assertThrows(TeamNotFoundException.class, () -> sponsorService.modify(sponsorId, sponsorInDto));
    }

    @Test
    public void testRemoveSponsor() throws SponsorNotFoundException {
        long sponsorId = 1;

        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        // Simula que el ciclista existe
        when(sponsorRepository.findById(sponsorId)).thenReturn(Optional.of(sponsor));

        // Act
        sponsorService.remove(sponsorId);

        // Assert
        verify(sponsorRepository).findById(sponsorId);
        verify(sponsorRepository).deleteById(sponsorId);
    }

    @Test
    public void testRemoveSponsorNotFound() throws CyclistNotFoundException {
        long sponsorId = 1;

        Sponsor sponsor = new Sponsor();
        sponsor.setId(sponsorId);

        try {
            when(sponsorRepository.findById(sponsorId).orElseThrow(SponsorNotFoundException::new))
                    .thenThrow(SponsorNotFoundException.class);
        } catch (SponsorNotFoundException e) {

        }
        assertThrows(SponsorNotFoundException.class, () -> sponsorService.remove(sponsorId));
    }

}
