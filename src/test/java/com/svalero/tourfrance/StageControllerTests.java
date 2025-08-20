package com.svalero.tourfrance;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.svalero.tourfrance.domain.dto.ErrorResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.svalero.tourfrance.domain.dto.*;

import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.repository.StageRepository;

import com.svalero.tourfrance.service.StageService;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.svalero.tourfrance.controller.StageController;


@WebMvcTest(StageController.class)
public class StageControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StageService stageService;

    @MockitoBean
    private StageRepository stageRepository;

    @Test
    public void testGetAllWithinParametersReturnOk() throws Exception {
        List<StageOutDto> mockSTageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(1, "Monaco", "Niza", "llana", 1800, 200, false, LocalDate.now()),
                new StageOutDto(1, "Formigal", "Lourdes", "montaña", 4500, 150, true, LocalDate.now())

        );

        when(stageService.getAll("", "", "")).thenReturn(mockSTageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(3, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByDepartureReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("Sant Luz", "", "")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("departure", "Sant Luz")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByArrivalReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("", "Gavarnie", "")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("arrival", "Gavarnie")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByTypeReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("", "", "montaña")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("type", "montaña")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByDepartureAndArrivalReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("Sant Luz", "Gavarnie", "")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("departure", "Sant Luz")
                        .queryParam("arrival", "Gavarnie")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByDepartureAndTypeReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("Sant Luz", "", "montaña")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("departure", "Sant Luz")
                        .queryParam("type", "montaña")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByArrivalAndTypeReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("", "Gavarnie", "montaña")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("arrival", "Gavarnie")
                        .queryParam("type", "montaña")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetAllByDepartureAndArrivalAndTypeReturnOk() throws Exception {
        List<StageOutDto> mockStageDtoList = List.of(
                new StageOutDto(1, "Sant Luz", "Gavarnie", "montaña", 4000, 140, true, LocalDate.now()),
                new StageOutDto(3, "Sant Luz", "Niza", "llana", 1800, 200, false, LocalDate.now())
        );

        when(stageService.getAll("Sant Luz", "Gavarnie", "montaña")).thenReturn(mockStageDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages")
                        .queryParam("departure", "Sant Luz")
                        .queryParam("arrival", "Gavarnie")
                        .queryParam("type", "montaña")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<StageOutDto> stageListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(stageListResponse);
        assertEquals(2, stageListResponse.size());
        assertEquals("Sant Luz", stageListResponse.getFirst().getDeparture());
        assertEquals("Gavarnie", stageListResponse.getFirst().getArrival());
        assertEquals("montaña", stageListResponse.getFirst().getType());

    }

    @Test
    public void testGetStageNotFound() throws Exception {
        when(stageService.get(1)).thenThrow(new StageNotFoundException());

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/stages/{stageId}", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getCode());
        assertEquals("The stage does not exist", errorResponse.getMessage());
    }

    @Test
    public void testAddStageCreated() throws Exception {
        String departure = "Sant Luz";
        String arrival = "Gavarnie";
        String type = "montaña";
        int elevation = 4000;
        float kilometers = 200;
        boolean mountainStage = true;

        StageOutDto stageOutDto = new StageOutDto(1, departure, arrival, type, elevation, kilometers, true, LocalDate.now());
        StageRegistrationDto stageRegistrationDto = new StageRegistrationDto(departure, arrival, type, elevation, kilometers, mountainStage, LocalDate.now());
        when(stageService.add(stageRegistrationDto)).thenReturn(stageOutDto);
        String requestBody = objectMapper.writeValueAsString(stageRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/stages")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.departure").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrival").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.elevation").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        StageOutDto responseStageOutDto = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(responseStageOutDto);
        assertEquals(departure, responseStageOutDto.getDeparture());
        assertEquals(arrival, responseStageOutDto.getArrival());
        assertEquals(type, responseStageOutDto.getType());
        assertEquals(elevation, responseStageOutDto.getElevation());
    }
    @Test
    public void testAddStageValidationError() throws Exception{
        String departure = null;
        String arrival = null;
        String type = "montaña";
        int elevation = 4000;
        float kilometers = -200;
        boolean mountainStage = true;

        StageRegistrationDto stageRegistrationDto = new StageRegistrationDto(departure, arrival, type, elevation, kilometers, mountainStage, LocalDate.now());

        String requestBody = objectMapper.writeValueAsString(stageRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/stages")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
        System.out.println("Errores de validación: " + errorResponse.getErrorMessages());
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getCode());
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals("El campo departure es obligatorio", errorResponse.getErrorMessages().get("departure"));
        assertEquals("El campo arrival es obligatorio", errorResponse.getErrorMessages().get("arrival"));
        assertEquals("must be greater than or equal to 0", errorResponse.getErrorMessages().get("kilometers"));

    }

    @Test
    public void testRemoveStageNoContent() throws Exception {
        long stageId = 1L;

        // Simulamos que el servicio no lanza excepción al eliminar
        doNothing().when(stageService).remove(stageId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/stages/{stageId}", stageId))
                .andExpect(status().isNoContent());

        verify(stageService, times(1)).remove(stageId);
    }

    @Test
    public void testRemoveStageNotFound() throws Exception {
        long stageId = 999L;

        // Simulamos que el servicio lanza CyclistNotFoundException
        doThrow(new StageNotFoundException()).when(stageService).remove(stageId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/stages/{stageId}", stageId))
                .andExpect(status().isNotFound());

        verify(stageService, times(1)).remove(stageId);
    }

    @Test
    public void testModifyStageOk() throws Exception {
        long stageId = 1L;

        StageInDto stageInDto = new StageInDto(
                "Sant Luz",           // departure
                "Gavarnie",             // arrival
                "montaña",               // type
                4000,                      // elevation
                200,                        // kilometers
                true,                       // mountainStage
                LocalDate.of(1990, 5, 1) // birthdate
        );

        StageOutDto modifiedStage = new StageOutDto(
                stageId, "Sant Luz", "Gavarnie", "montaña", 4000, 200, true,
                LocalDate.of(1990, 5, 1)
        );

        when(stageService.modify(eq(stageId), any(StageInDto.class)))
                .thenReturn(modifiedStage);

        String requestBody = objectMapper.writeValueAsString(stageInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/stages/{stageId}", stageId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(stageId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.departure").value("Sant Luz"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.arrival").value("Gavarnie"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type").value("montaña"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.elevation").value(4000));

        verify(stageService, times(1)).modify(eq(stageId), any(StageInDto.class));
    }

    @Test
    public void testModifyStageNotFound() throws Exception {
        long stageId = 999L;

        StageInDto stageInDto = new StageInDto(
                "Sant Luz",           // departure
                "Gavarnie",             // arrival
                "montaña",               // type
                4000,                      // elevation
                200,                        // kilometers
                true,                       // mountainStage
                LocalDate.of(1990, 5, 1) // birthdate
        );

        // El servicio lanza la excepción
        when(stageService.modify(eq(stageId), any(StageInDto.class)))
                .thenThrow(new StageNotFoundException());

        String requestBody = objectMapper.writeValueAsString(stageInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/stages/{stageId}", stageId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(stageService, times(1)).modify(eq(stageId), any(StageInDto.class));
    }

    @Test
    public void modifyStageReturn400() throws Exception {

        StageInDto missingDto = new StageInDto();
        missingDto.setElevation(4800);
        missingDto.setMountainStage(true);
        missingDto.setKilometers(-200);

        MvcResult result = mockMvc.perform(put("/stages/{stageId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(missingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages.departure").value("El campo departure es obligatorio"))
                .andExpect(jsonPath("$.errorMessages.arrival").value("El campo arrival es obligatorio"))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
        verify(stageService, never()).modify(anyLong(), any(StageInDto.class));

    }
}
