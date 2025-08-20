package com.svalero.tourfrance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.tourfrance.controller.ClimbController;
import com.svalero.tourfrance.controller.TeamController;
import com.svalero.tourfrance.domain.dto.*;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.TeamRepository;
import com.svalero.tourfrance.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeamController.class)
public class TeamControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TeamService teamService;

    @MockitoBean
    private TeamRepository teamRepository;

    //test para getall
    @Test
    public void testGetAllWithinParametersReturnOk() throws Exception {
        List<TeamOutDto> mockTeamDtoList = List.of(
                new TeamOutDto(1, "Visma", "Dinamarca", "visma@visma.com", 104, LocalDate.now()),
                new TeamOutDto(2, "UAE", "Emiratos Arabes", "uae@uae.com", 90, LocalDate.now()),
                new TeamOutDto(2, "Movistar", "España", "movistar@movistar.com", 30, LocalDate.now())
        );

        when(teamService.getAll("", "", "")).thenReturn(mockTeamDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/teams")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse(). getContentAsString();
        List<TeamOutDto> teamListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(teamListResponse);
        assertEquals(3, teamListResponse.size());
        assertEquals("Visma", teamListResponse.getFirst().getName());
        assertEquals("Dinamarca", teamListResponse.getFirst().getCountry());
    }
    @Test
    public void testGetAllByNameReturnOk() throws Exception {
        List<TeamOutDto> mockTeamDtoList = List.of(
                new TeamOutDto(1, "Visma", "Dinamarca", "visma@visma.com", 300, LocalDate.now()),
                new TeamOutDto(3, "Visma", "Denmark", "visma@visma.com", 300, LocalDate.now())
        );

        when(teamService.getAll("Visma", "", "")).thenReturn(mockTeamDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/teams")
                        .queryParam("name", "Visma")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<TeamOutDto> teamListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(teamListResponse);
        assertEquals(2, teamListResponse.size());
        assertEquals("Visma", teamListResponse.getFirst().getName());
        assertEquals("Dinamarca", teamListResponse.getFirst().getCountry());
        assertEquals("Denmark", teamListResponse.getLast().getCountry());
    }
    @Test
    public void testGetTeamNotFound() throws Exception {
        when(teamService.get(1)).thenThrow(new TeamNotFoundException());

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/teams/{teamId}", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getCode());
        assertEquals("The team does not exist", errorResponse.getMessage());
    }
    @Test
    public void testAddTeamCreated() throws Exception {
        String name = "Visma";
        String country = "Dinamarca";
        String email = "visma@visma.com";
        int budget = 300;

        TeamOutDto teamOutDto = new TeamOutDto(1, name, country, email, budget, LocalDate.now());
        TeamRegistrationDto teamRegistrationDto = new TeamRegistrationDto(name, country, email, budget, LocalDate.now());
        when(teamService.add(teamRegistrationDto)).thenReturn(teamOutDto);
        String requestBody = objectMapper.writeValueAsString(teamRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams")
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.budget").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        TeamOutDto responseTeamOutDto = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(responseTeamOutDto);
        assertEquals(name, responseTeamOutDto.getName());
        assertEquals(country, responseTeamOutDto.getCountry());
        assertEquals(email, responseTeamOutDto.getEmail());
        assertEquals(budget, responseTeamOutDto.getBudget());
    }
    @Test
    public void testAddTeamValidationError() throws Exception{
        String name = null;
        String country = null;
        String email = "visma@visma.com";
        int budget = -104;

        TeamRegistrationDto teamRegistrationDto = new TeamRegistrationDto(name, country, email, budget,LocalDate.now());

        String requestBody = objectMapper.writeValueAsString(teamRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams")
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
        assertEquals("El campo name es obligatorio", errorResponse.getErrorMessages().get("name"));
        assertEquals("El campo country es obligatorio", errorResponse.getErrorMessages().get("country"));
        assertEquals("must be greater than or equal to 0", errorResponse.getErrorMessages().get("budget"));

    }

    // testRemoveTeamNoContent

    @Test
    public void testRemoveTeamNoContent() throws Exception {
        long teamId = 1L;

        doNothing().when(teamService).remove(teamId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/teams/{teamId}", teamId))
                .andExpect(status().isNoContent());

        verify(teamService, times(1)).remove(teamId);
    }


    //TODO testRemoveTeamNotFound


    //TODO testModifyTeamOk
    //TODO testModifyTeamNotFound
    //TODO testmodifyTeamReturn400

}
