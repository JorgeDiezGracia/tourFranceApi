package com.svalero.tourfrance;

import com.svalero.tourfrance.domain.Climb;
import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.Stage;
import com.svalero.tourfrance.domain.Team;
import com.svalero.tourfrance.domain.dto.*;
import com.svalero.tourfrance.exception.ClimbNotFoundException;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
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

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testAdd(){
        long stageId = 1;
        Stage mockStage = new Stage(stageId, "Saint Luz", "Troumusse", "montaña", 4200, 150, false, LocalDate.now(), new ArrayList<>());
        ClimbRegistrationDto climbRegistrationDto = new ClimbRegistrationDto("Tourmalet", "Hors Category", "Francia", 1900, 1000, true, LocalDate.now(), 48, 47);

        Climb mockClimb = new Climb(0, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 85, 45, null);
        ClimbOutDto mockClimbOutDto = new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1650, 785, true, LocalDate.now(), 1);

        when(stageRepository.findById(stageId)).thenReturn(Optional.of(mockStage));
        when(modelMapper.map(climbRegistrationDto, Climb.class)).thenReturn(mockClimb);
        when(modelMapper.map(mockClimb, ClimbOutDto.class)).thenReturn(mockClimbOutDto);

        try {
            climbService.add(stageId, climbRegistrationDto);
        } catch (StageNotFoundException unfe) {

        }

        assertEquals(1, mockClimbOutDto.getId());
        assertEquals("Tourmalet", mockClimbOutDto.getName());
        assertEquals("Francia", mockClimbOutDto.getRegion());
        assertEquals("Hors Category", mockClimbOutDto.getCategory());
        assertEquals(1650, mockClimbOutDto.getAltitude());
        assertEquals(785, mockClimbOutDto.getSlope());
        assertEquals(LocalDate.now(), mockClimbOutDto.getLastAscent());
        assertEquals(1, mockClimbOutDto.getStageId());

        verify(climbRepository, Mockito.times(1)).save(mockClimb);

    }

    @Test
    void testModify() throws ClimbNotFoundException, StageNotFoundException {
        // Arrange
        long climbId = 1;
        long stageId = 100;

        ClimbInDto climbInDto = new ClimbInDto();
        climbInDto.setStageId(stageId);
        climbInDto.setName("Updated Name");

        Climb existingClimb = new Climb();
        existingClimb.setId(climbId);

        Stage stage = new Stage();
        stage.setId(stageId);

        Climb updatedClimb = new Climb(); // after mapping
        updatedClimb.setId(climbId);
        updatedClimb.setStage(stage);
        updatedClimb.setName("Updated Name");

        ClimbOutDto expectedOutDto = new ClimbOutDto();
        expectedOutDto.setId(climbId);
        expectedOutDto.setName("Updated Name");

        // Mocking behavior
        when(climbRepository.findById(climbId)).thenReturn(Optional.of(existingClimb));
        when(stageRepository.findById(stageId)).thenReturn(Optional.of(stage));

        // ✅ Mock de TypeMap para evitar NullPointer
        TypeMap<ClimbInDto, Climb> mockTypeMap = mock(TypeMap.class);
        when(modelMapper.typeMap(ClimbInDto.class, Climb.class)).thenReturn(mockTypeMap);


        // Mapping input DTO to entity
        doAnswer(invocation -> {
            ClimbInDto source = invocation.getArgument(0);
            Climb target = invocation.getArgument(1);
            target.setName(source.getName()); // simulate mapping
            return null;
        }).when(modelMapper).map(eq(climbInDto), eq(existingClimb));

        // Output DTO mapping
        when(modelMapper.map(existingClimb, ClimbOutDto.class)).thenReturn(expectedOutDto);

        // Act
        ClimbOutDto result = climbService.modify(climbId, climbInDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOutDto.getId(), result.getId());
        assertEquals(expectedOutDto.getName(), result.getName());

        verify(climbRepository).findById(climbId);
        verify(stageRepository).findById(stageId);
        verify(modelMapper).map(climbInDto, existingClimb);
        verify(climbRepository).save(existingClimb);
        verify(modelMapper).map(existingClimb, ClimbOutDto.class);
    }

    @Test
    public void testAddStageNotFound() throws StageNotFoundException {
        long stageId = 150;
        ClimbRegistrationDto climbRegistrationDto = new ClimbRegistrationDto("Tourmalet", "Hors Category", "Francia", 1700,1100, true, LocalDate.now(), 47, 45);

        try {
            when(stageRepository.findById(stageId).orElseThrow(StageNotFoundException::new)).thenThrow(StageNotFoundException.class);
        } catch (StageNotFoundException unfe) {
        }
        assertThrows(StageNotFoundException.class, () -> climbService.add(stageId, climbRegistrationDto));
    }

    @Test
    public void testModifyClimbNotFound() throws ClimbNotFoundException, StageNotFoundException {
        long climbId = 1;
        ClimbInDto climbInDto = new ClimbInDto();
        climbInDto.setStageId(100);

        try {
            when(climbRepository.findById(climbId).orElseThrow(ClimbNotFoundException::new))
                    .thenThrow(ClimbNotFoundException.class);
        } catch (ClimbNotFoundException e) {

        }

        assertThrows(ClimbNotFoundException.class, () -> climbService.modify(climbId, climbInDto));
    }
    @Test
    public void testModifyStageNotFound() throws ClimbNotFoundException, StageNotFoundException {
        long climbId = 1;
        long stageId = 200;

        ClimbInDto climbInDto = new ClimbInDto();
        climbInDto.setStageId(stageId);

        Climb existingClimb = new Climb();
        existingClimb.setId(climbId);

        when(climbRepository.findById(climbId)).thenReturn(Optional.of(existingClimb));

        try {
            when(stageRepository.findById(stageId).orElseThrow(StageNotFoundException::new))
                    .thenThrow(StageNotFoundException.class);
        } catch (StageNotFoundException e) {

        }

        assertThrows(StageNotFoundException.class, () -> climbService.modify(climbId, climbInDto));
    }

    @Test
    public void testRemoveClimb() throws ClimbNotFoundException {
        long climbId = 1;

        Climb climb = new Climb();
        climb.setId(climbId);

        // Simula que el ciclista existe
        when(climbRepository.findById(climbId)).thenReturn(Optional.of(climb));

        // Act
        climbService.remove(climbId);

        // Assert
        verify(climbRepository).findById(climbId);
        verify(climbRepository).deleteById(climbId);
    }


}
