package com.svalero.tourfrance;

import com.svalero.tourfrance.domain.Climb;
import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.dto.ClimbOutDto;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.repository.ClimbRepository;
import com.svalero.tourfrance.repository.StageRepository;
import com.svalero.tourfrance.service.ClimbService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.TypeToken;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import static javax.management.Query.times;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClimbServiceTests {

    @InjectMocks
    private ClimbService climbService;

    @Mock
    private ClimbRepository climbRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private StageRepository stageRepository;

    @Test
    public void testGetAll() {
        List<Climb> mockClimbList = List.of(
                new Climb (1,"Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null),
                new Climb (2,"Le Madeleine", "Hors Category", "Suiza", 1890, 900, true, LocalDate.now(), 86, 47, null),
                new Climb (3,"Gavarnie", "Hors Category", "Francia", 2100, 1100, false, LocalDate.now(), 89, 42, null)
        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1),
                new ClimbOutDto(1, "Le Madeleine", "Hors Category", "Suiza", 1890, 900, true, LocalDate.now(), 2),
                new ClimbOutDto(1, "Gavarnie", "Hors Category", "Francia", 2100, 1100, false, LocalDate.now(), 3)
        );

        when(climbRepository.findAll()).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        List<ClimbOutDto> climbList = climbService.getAll("", "", "");
                assertEquals(3, climbList.size());
                assertEquals("Tourmalet", climbList.getFirst().getName());
                assertEquals("Gavarnie", climbList.getLast().getName());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findByRegion("");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("","");
        verify(climbRepository, Mockito.times(1)).findAll();

    }

    @Test
    public void testGetAllByName() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByName("Tourmalet")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("Tourmalet", "", "");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Hors Category", climbList.getFirst().getCategory());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(1)).findByName("Tourmalet");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findByRegion("");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findAll();

    }

    @Test
    public void testGetAllByCategory() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByCategory("Hors Category")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("", "Hors Category", "");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Hors Category", climbList.getFirst().getCategory());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(1)).findByCategory("Hors Category");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findByRegion("");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findAll();
    }

    @Test
    public void testGetAllByRegion() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByRegion("Francia")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("", "", "Francia");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Francia", climbList.getFirst().getRegion());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("", "");
        verify(climbRepository, Mockito.times(1)).findByRegion("Francia");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findAll();
    }

    @Test
    public void testGetAllByNameAndCategory() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByNameAndCategory("Tourmalet", "Hors Category")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("Tourmalet", "Hors Category", "");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Francia", climbList.getFirst().getRegion());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(1)).findByNameAndCategory("Tourmalet", "Hors Category");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findByRegion("Francia");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findAll();
    }

    @Test
    public void testGetAllByNameAndRegion() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByNameAndRegion("Tourmalet", "Francia")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("Tourmalet", "", "Francia");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Francia", climbList.getFirst().getRegion());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(1)).findByNameAndRegion("Tourmalet", "Francia");
        verify(climbRepository, Mockito.times(0)).findByRegion("Francia");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findAll();
    }

    @Test
    public void testGetAllByCategoryAndRegion() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByCategoryAndRegion("Hors Category", "Francia")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("", "Hors Category", "Francia");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Francia", climbList.getFirst().getRegion());

        verify(climbRepository, Mockito.times(0)).findByNameAndCategoryAndRegion("", "", "");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("Tourmalet", "Francia");
        verify(climbRepository, Mockito.times(0)).findByRegion("Francia");
        verify(climbRepository, Mockito.times(1)).findByCategoryAndRegion("Hors Category", "Francia");
        verify(climbRepository, Mockito.times(0)).findAll();
    }

    @Test
    public void testGetAllByNameAndCategoryAndRegion() {
        List<Climb> mockClimbList = List.of(
                new Climb(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null)

        );
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1)

        );

        when(climbRepository.findByNameAndCategoryAndRegion("Tourmalet", "Hors Category", "Francia")).thenReturn(mockClimbList);
        when(modelMapper.map(mockClimbList, new TypeToken<List<ClimbOutDto>>() {
        }.getType())).thenReturn(mockClimbDtoList);

        // listado de ciclistas sin aplicar ningún filtro
        List<ClimbOutDto> climbList = climbService.getAll("Tourmalet", "Hors Category", "Francia");
        assertEquals(1, climbList.size());
        assertEquals("Tourmalet", climbList.getFirst().getName());
        assertEquals("Francia", climbList.getFirst().getRegion());

        verify(climbRepository, Mockito.times(1)).findByNameAndCategoryAndRegion("Tourmalet", "Hors Category", "Francia");
        verify(climbRepository, Mockito.times(0)).findByName("");
        verify(climbRepository, Mockito.times(0)).findByCategory("");
        verify(climbRepository, Mockito.times(0)).findByNameAndCategory("", "");
        verify(climbRepository, Mockito.times(0)).findByNameAndRegion("Tourmalet", "Francia");
        verify(climbRepository, Mockito.times(0)).findByRegion("Francia");
        verify(climbRepository, Mockito.times(0)).findByCategoryAndRegion("", "");
        verify(climbRepository, Mockito.times(0)).findAll();
    }
}
