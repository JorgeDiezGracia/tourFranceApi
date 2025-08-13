package com.svalero.tourfrance;

import com.svalero.tourfrance.domain.Stage;
import com.svalero.tourfrance.domain.dto.*;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.repository.ClimbRepository;
import com.svalero.tourfrance.repository.StageRepository;
import com.svalero.tourfrance.service.StageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ExpressionMap;
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
public class StageServiceTests {

    @InjectMocks
    private StageService stageService;

    @Mock
    private StageRepository stageRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ClimbRepository climbRepository;

    @Test
    public void testGetAll(){
        List<Stage> mockStageList = List.of(
                new Stage(1, "Sant Luz", "Gavarnie", "montaña", 4500, 140, true, LocalDate.now(), new ArrayList<>()),
                new Stage(2, "Monaco", "Niza", "llana", 1800, 200, false, LocalDate.now(), new ArrayList<>()),
                new Stage(3, "Formigal", "Lourdes", "montaña", 3000, 190, true, LocalDate.now(), new ArrayList<>())
        );

       List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4500, 140, true, LocalDate.now()),
                new StageOutDto(2, "Monaco", "Niza", "llana", 1800, 200, false, LocalDate.now()),
                new StageOutDto(3, "Formigal", "Laruns", "montaña", 4000, 110, true, LocalDate.now())
        );
        when(stageRepository.findAll()).thenReturn(mockStageList);
        when(modelMapper.map(mockStageList, new TypeToken<List<StageOutDto>>(){
        }.getType())).thenReturn(mockStageDtoList);

        List<StageOutDto> stageList = stageService.getAll("", "", "");
        assertEquals(3, stageList.size());
        assertEquals("Sant Luz", stageList.getFirst().getDeparture());
        assertEquals("Formigal", stageList.getLast().getDeparture());

        verify(stageRepository, times(1)).findAll();
        verify(stageRepository, times(0)).findByDeparture("");
        verify(stageRepository, times(0)).findByArrival("");
        verify(stageRepository, times(0)).findByType("");
        verify(stageRepository, times(0)).findByDepartureAndArrival("","");
        verify(stageRepository, times(0)).findByDepartureAndType("", "");
        verify(stageRepository, times(0)).findByArrivalAndType("","");
        verify(stageRepository, times(0)).findByDepartureAndArrivalAndType("","","");
    }

    @Test
    public void testGetAllByDeparture() {
        List<Stage> mockStageList = List.of(
                new Stage(1, "Sant Luz", "Gavarnie", "montaña", 4000, 150, true, LocalDate.now(), new ArrayList<>())
        );
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 150, true, LocalDate.now())
        );

        when(stageRepository.findByDeparture("Sant Luz")).thenReturn(mockStageList);
        when(modelMapper.map(mockStageList, new TypeToken<List<StageOutDto>>() {
        }.getType())).thenReturn(mockStageDtoList);

        // listado de stage sin aplicar ningún filtro
        List<StageOutDto> stageList = stageService.getAll("Sant Luz", "", "");
        assertEquals(1, stageList.size());
        assertEquals("Sant Luz", stageList.getFirst().getDeparture());
        assertEquals("montaña", stageList.getFirst().getType());

        //mirar todas las opciones según el if
        verify(stageRepository, times(0)).findByDepartureAndArrivalAndType("", "", "");
        verify(stageRepository, times(0)).findByArrivalAndType("", "");
        verify(stageRepository, times(0)).findByDepartureAndType("", "");
        verify(stageRepository, times(0)).findByType("");
        verify(stageRepository, times(0)).findAll();
        verify(stageRepository, times(0)).findByArrival("Gavarnie");
        verify(stageRepository, times(0)).findByDepartureAndArrival("Sant Luz", "");
        verify(stageRepository, times(1)).findByDeparture("Sant Luz");
    }

    @Test
    public void testGetAllByArrival() {
        List<Stage> mockStageList = List.of(
                new Stage(1, "Sant Luz", "Gavarnie", "montaña", 4000, 150, true, LocalDate.now(), new ArrayList<>())
        );
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 150, true, LocalDate.now())
        );

        when(stageRepository.findByArrival("Gavarnie")).thenReturn(mockStageList);
        when(modelMapper.map(mockStageList, new TypeToken<List<StageOutDto>>() {
        }.getType())).thenReturn(mockStageDtoList);

        // listado de stage sin aplicar ningún filtro
        List<StageOutDto> stageList = stageService.getAll("", "Gavarnie", "");
        assertEquals(1, stageList.size());
        assertEquals("Sant Luz", stageList.getFirst().getDeparture());
        assertEquals("montaña", stageList.getFirst().getType());

        //mirar todas las opciones según el if
        verify(stageRepository, times(0)).findByDepartureAndArrivalAndType("", "", "");
        verify(stageRepository, times(0)).findByArrivalAndType("", "");
        verify(stageRepository, times(0)).findByDepartureAndType("", "");
        verify(stageRepository, times(0)).findByType("");
        verify(stageRepository, times(0)).findAll();
        verify(stageRepository, times(1)).findByArrival("Gavarnie");
        verify(stageRepository, times(0)).findByDepartureAndArrival("Sant Luz", "");
        verify(stageRepository, times(0)).findByDeparture("Sant Luz");
    }



    @Test
    public void testGetStageByIdFound() throws StageNotFoundException {
        long stageId = 1L;
        Stage mockStage = new Stage();
        mockStage.setId(stageId);
        mockStage.setDeparture("Sant Luz");

        when(stageRepository.findById(stageId)).thenReturn(Optional.of(mockStage));

        Stage result = stageService.get(stageId);

        assertNotNull(result);
        assertEquals(stageId, result.getId());
        assertEquals("Sant Luz", result.getDeparture());
    }

    @Test
    public void testGetStageByIdNotFound() {
        long stageId = 99L;
        when(stageRepository.findById(stageId)).thenReturn(Optional.empty());

        assertThrows(StageNotFoundException.class, () -> {
            stageService.get(stageId);
        });
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void testAdd() {
        StageRegistrationDto stageRegistrationDto = new StageRegistrationDto("Sant Luz", "Gavarnie", "montaña",4500, 140, true, LocalDate.now());
        Stage mockStage = new Stage(1, "Sant Luz", "Gavarnie", "montaña", 4500, 140, true, LocalDate.now(), new ArrayList<>());
        StageOutDto mockStageOutDto = new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 1400, 140, true, LocalDate.now());

        when(modelMapper.map(stageRegistrationDto, Stage.class)).thenReturn(mockStage);
        when(stageRepository.save(mockStage)).thenReturn(mockStage);
        when(modelMapper.map(mockStage, StageOutDto.class)).thenReturn(mockStageOutDto);

        StageOutDto result = stageService.add(stageRegistrationDto);

        assertEquals(1, result.getId());
        assertEquals("Sant Luz", result.getDeparture());
        assertEquals("Gavarnie", result.getArrival());
        assertEquals("montaña", result.getType());
        assertEquals(1400, result.getElevation());
        assertEquals(LocalDate.now(), result.getStageDate());

        verify(stageRepository, times(1)).save(mockStage);
    }

    @Test
    public void testModifyStage() throws StageNotFoundException {

        long stageId = 1;
        StageInDto stageInDto = new StageInDto();
        stageInDto.setDeparture("Updated Departure");
        stageInDto.setArrival("Updated Arrival");
        stageInDto.setType("Updated Type");
        stageInDto.setElevation(4000);

        Stage existingStage = new Stage();
        existingStage.setId(stageId);
        existingStage.setDeparture("Old Departure");
        existingStage.setArrival("Old Arrival");
        existingStage.setType("Old Type");
        existingStage.setElevation(2000);

        StageOutDto expectedOutDto = new StageOutDto();
        expectedOutDto.setId(stageId);
        expectedOutDto.setDeparture("Updated Departure");
        expectedOutDto.setArrival("Updated Arrival");
        expectedOutDto.setType("Updated Type");
        expectedOutDto.setElevation(4000);

        when(stageRepository.findById(stageId)).thenReturn(Optional.of(existingStage));

        @SuppressWarnings("unchecked")
        TypeMap<StageInDto, Stage> mockTypeMap = mock(TypeMap.class);
        when(modelMapper.typeMap(StageInDto.class, Stage.class)).thenReturn(mockTypeMap);

        // Aquí mockeamos addMappings para que devuelva el mismo mock (encadenamiento)
        when(mockTypeMap.addMappings(
                ArgumentMatchers.<ExpressionMap<StageInDto, Stage>>any()
        )).thenReturn(mockTypeMap);

        // Mockear mapping que modifica el objeto destino
        doAnswer(invocation -> {
            StageInDto source = invocation.getArgument(0);
            Stage target = invocation.getArgument(1);
            target.setDeparture(source.getDeparture());
            target.setArrival(source.getArrival());
            target.setType(source.getType());
            target.setElevation(source.getElevation());
            return null;
        }).when(modelMapper).map(eq(stageInDto), eq(existingStage));

        when(modelMapper.map(existingStage, StageOutDto.class)).thenReturn(expectedOutDto);

        when(stageRepository.save(existingStage)).thenReturn(existingStage);

        // Act
        StageOutDto result = stageService.modify(stageId, stageInDto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOutDto.getId(), result.getId());
        assertEquals(expectedOutDto.getDeparture(), result.getDeparture());
        assertEquals(expectedOutDto.getArrival(), result.getArrival());
        assertEquals(expectedOutDto.getType(), result.getType());
        assertEquals(expectedOutDto.getElevation(), result.getElevation());

        verify(stageRepository).findById(stageId);
        verify(modelMapper, times(2)).map(stageInDto, existingStage);
        verify(stageRepository).save(existingStage);
        verify(modelMapper).map(existingStage, StageOutDto.class);
    }

    @Test
    public void testModifyStageNotFound() throws StageNotFoundException {
        long stageId = 1;
        StageInDto stageInDto = new StageInDto();

        try {
            when(stageRepository.findById(stageId).orElseThrow(StageNotFoundException::new))
                    .thenThrow(StageNotFoundException.class);
        } catch (StageNotFoundException e) {

        }

        assertThrows(StageNotFoundException.class, () -> stageService.modify(stageId, stageInDto));
    }

    @Test
    public void testRemoveStage() throws StageNotFoundException {
        long stageId = 1;

        Stage stage = new Stage();
        stage.setId(stageId);

        // Simula que el stage existe
        when(stageRepository.findById(stageId)).thenReturn(Optional.of(stage));

        // Act
        stageService.remove(stageId);

        // Assert
        verify(stageRepository).findById(stageId);
        verify(stageRepository).deleteById(stageId);
    }


}


