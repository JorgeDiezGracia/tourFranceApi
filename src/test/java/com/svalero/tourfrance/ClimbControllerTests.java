package com.svalero.tourfrance;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.svalero.tourfrance.domain.dto.ErrorResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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


import com.svalero.tourfrance.controller.ClimbController;
import com.svalero.tourfrance.domain.Climb;
import com.svalero.tourfrance.domain.dto.*;
import com.svalero.tourfrance.domain.dto.ClimbOutDto;
import com.svalero.tourfrance.exception.ClimbNotFoundException;
import com.svalero.tourfrance.exception.StageNotFoundException;
import com.svalero.tourfrance.repository.StageRepository;
import com.svalero.tourfrance.service.ClimbService;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ClimbController.class)
public class ClimbControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClimbService climbService;

    @MockitoBean
    private StageRepository stageRepository;

    //test para getAll
    @Test
    public void testGetAllWithinParametersReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16),
                new ClimbOutDto(3, "Cirque Troumousse", "Hors Category", "Francia", 1980, 1300, false, LocalDate.now(), 17)
        );
        when(climbService.getAll("", "", "")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(3, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Cirque Troumousse", climbListResponse.getLast().getName());
    }

    @Test
    public void testGetAllByNameReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("Tourmalet", "", "")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("name", "Tourmalet")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
    }

    @Test
    public void testGetAllByCategoryReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("", "Hors Category", "")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("category", "Hors Category")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Francia", climbListResponse.getFirst().getRegion());
    }

    @Test
    public void testGetAllByRegionReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("", "", "Francia")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("region", "Francia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Francia", climbListResponse.getFirst().getRegion());
    }

    @Test
    public void testGetAllByNameAndCategoryReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("Tourmalet", "Hors Category", "")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("name", "Tourmalet")
                        .queryParam("category", "Hors Category")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Francia", climbListResponse.getFirst().getRegion());
    }

    @Test
    public void testGetAllByNameAndRegionReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("Tourmalet", "", "Francia")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("name", "Tourmalet")
                        .queryParam("region", "Francia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Francia", climbListResponse.getFirst().getRegion());
    }

    @Test
    public void testGetAllByCategoryAndRegionReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("", "Hors Category", "Francia")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("category", "Hors Category")
                        .queryParam("region", "Francia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Francia", climbListResponse.getFirst().getRegion());
    }

    @Test
    public void testGetAllByNameAndCategoryAndRegionReturnOk() throws Exception {
        List<ClimbOutDto> mockClimbDtoList = List.of(
                new ClimbOutDto(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 15),
                new ClimbOutDto(2, "Le Madeleine", "Hors Category", "Suiza", 2140, 1400, true, LocalDate.now(), 16)
        );

        when(climbService.getAll("Tourmalet", "Hors Category", "Francia")).thenReturn(mockClimbDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs")
                        .queryParam("name", "Tourmalet")
                        .queryParam("category", "Hors Category")
                        .queryParam("region", "Francia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<ClimbOutDto> climbListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbListResponse);
        assertEquals(2, climbListResponse.size());
        assertEquals("Tourmalet", climbListResponse.getFirst().getName());
        assertEquals("Hors Category", climbListResponse.getFirst().getCategory());
        assertEquals("Francia", climbListResponse.getFirst().getRegion());
    }

    @Test

    public void testGetClimbReturnOk() throws Exception {
        Climb mockClimb = new Climb(1, "Tourmalet", "Hors Category", "Francia", 1790, 1100, false, LocalDate.now(), 47, 49, null);

        when(climbService.get(1)).thenReturn(mockClimb);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs/{climbId}", "1")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        Climb climbResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(climbResponse);
        assertEquals("Tourmalet", climbResponse.getName());
        assertEquals("Hors Category", climbResponse.getCategory());
    }

    @Test
    public void testGetClimbNotFound() throws Exception {
        when(climbService.get(1)).thenThrow(new ClimbNotFoundException());

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/climbs/{climbId}", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getCode());
        assertEquals("The climb does not exist", errorResponse.getMessage());
    }

    @Test
    public void testAddClimbCreated() throws Exception {
        String name = "Tourmalet";
        String category = "Hors Category";
        String region = "Francia";
        int altitude = 1790;
        float slope = 1100;
        boolean goal = false;
        LocalDate lastAscent = LocalDate.now();
        long stageId = 15;


        ClimbOutDto climbOutDto = new ClimbOutDto(1, name, category, region, altitude, slope, goal, lastAscent, stageId);
        ClimbRegistrationDto climbRegistrationDto = new ClimbRegistrationDto(name, category, region, altitude, slope, false, LocalDate.now(), 47, 49);
        when(climbService.add(stageId, climbRegistrationDto)).thenReturn(climbOutDto);
        String requestBody = objectMapper.writeValueAsString(climbRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/stages/{stageId}/climbs", stageId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.stageId").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.lastAscent").exists())
                .andExpect(jsonPath("$.altitude").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ClimbOutDto responseClimbOutDto = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(responseClimbOutDto);
        assertEquals(name, responseClimbOutDto.getName());
        assertEquals(category, responseClimbOutDto.getCategory());
        assertEquals(region, responseClimbOutDto.getRegion());
        assertEquals(altitude, responseClimbOutDto.getAltitude());
        assertEquals(stageId, responseClimbOutDto.getStageId());
    }

    @Test
    public void testAddClimbTeamNotFound() throws Exception {
        String name = "Tourmalet";
        String category = "Hors Category";
        String region = "Francia";
        int altitude = 1980;
        long stageId = 15;

        ClimbRegistrationDto climbRegistrationDto = new ClimbRegistrationDto(name, category, region, altitude, 1100, false, LocalDate.now(), 47, 49);

        when(climbService.add(stageId, climbRegistrationDto)).thenThrow(new StageNotFoundException());

        String requestBody = objectMapper.writeValueAsString(climbRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/stages/{stageId}/climbs", stageId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
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
    public void testAddClimbValidationError() throws Exception {
        String name = null;
        String category = null;
        String region = "Francia";
        int altitude = -1100;
        long stageId = 15;

        ClimbRegistrationDto climbRegistrationDto = new ClimbRegistrationDto(name, category, region, altitude, 1100, false, LocalDate.now(), 47, 49);

        String requestBody = objectMapper.writeValueAsString(climbRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/stages/{staged}/climbs", stageId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getCode());
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals("El campo name es obligatorio", errorResponse.getErrorMessages().get("name"));
        assertEquals("El campo category es obligatorio", errorResponse.getErrorMessages().get("category"));
        assertEquals("must be greater than or equal to 0", errorResponse.getErrorMessages().get("altitude"));
    }

    @Test
    public void testRemoveClimbNoContent() throws Exception {
        long climbId = 1L;

        doNothing().when(climbService).remove(climbId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/climbs/{climbId}", climbId))
                .andExpect(status().isNoContent());

        verify(climbService, times(1)).remove(climbId);
    }

    @Test
    public void testRemoveClimbNotFound() throws Exception {
        long climbId = 999L;

        doThrow(new ClimbNotFoundException()).when(climbService).remove(climbId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/climbs/{climbId}", climbId))
                .andExpect(status().isNotFound());

        verify(climbService, times(1)).remove(climbId);
    }

    @Test
    public void testModifyClimbOk() throws Exception {
        long climbId = 1L;
        long stageId = 10L;

        ClimbInDto climbInDto = new ClimbInDto("Tourmalet", "Hors Category", "Francia", 1800, 900, LocalDate.now(), stageId);


        ClimbOutDto modifiedClimb = new ClimbOutDto(climbId, "Tourmalet", "Hors Category", "Francia", 1800, 900, true, LocalDate.now(), stageId);

        when(climbService.modify(eq(climbId), any(ClimbInDto.class)))
                .thenReturn(modifiedClimb);

        String requestBody = objectMapper.writeValueAsString(climbInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/climbs/{climbId}", climbId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(climbId))
                .andExpect(jsonPath("$.name").value("Tourmalet"))
                .andExpect(jsonPath("$.category").value("Hors Category"))
                .andExpect(jsonPath("$.region").value("Francia"))
                .andExpect(jsonPath("$.altitude").value(1800))
                .andExpect(jsonPath("$.stageId").value(stageId));

        verify(climbService, times(1)).modify(eq(climbId), any(ClimbInDto.class));

    }

    @Test
    public void testModifyClimbNotFound() throws Exception {
        long climbId = 999L;
        long stageId = 10L;

        ClimbInDto climbInDto = new ClimbInDto("Tourmalet", "Hors Category", "Francia", 1800, 900, LocalDate.now(), stageId);

        when(climbService.modify(eq(climbId), any(ClimbInDto.class)))
                .thenThrow(new ClimbNotFoundException());

        String requestBody = objectMapper.writeValueAsString(climbInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/climbs/{climbId}", climbId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(climbService, times(1)).modify(eq(climbId), any(ClimbInDto.class));
    }

    @Test
    public void testModifyStageNotFound() throws Exception {
        long climbId = 1L;
        long stageId = 9999L;

        ClimbInDto climbInDto = new ClimbInDto("Tourmalet", "Hors Category", "Francia", 1800, 900, LocalDate.now(), stageId);

        when(climbService.modify(eq(climbId), any(ClimbInDto.class)))
                .thenThrow(new StageNotFoundException());

        String requestBody = objectMapper.writeValueAsString(climbInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/climbs/{climbId}", climbId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(climbService, times(1)).modify(eq(climbId), any(ClimbInDto.class));
    }

    @Test
    public void modifyClimbReturn400() throws Exception {
        //caso 1
        ClimbInDto missingDto = new ClimbInDto();
        missingDto.setRegion("Francia");
        missingDto.setAltitude(1800);
        missingDto.setStageId(9999L);

        MvcResult result1 = mockMvc.perform(put("/climbs/{climbId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(missingDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessages.name").value("El campo name es obligatorio"))
                .andExpect(jsonPath("$.errorMessages.category").value("El campo category es obligatorio"))
                .andReturn();

        System.out.println(result1.getResponse().getContentAsString());
        verify(climbService, never()).modify(anyLong(), any(ClimbInDto.class));

    }
}
