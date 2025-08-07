package com.svalero.tourfrance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.tourfrance.controller.CyclistController;
import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.domain.dto.CyclistRegistrationDto;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.TeamRepository;
import com.svalero.tourfrance.service.CyclistService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CyclistController.class)
public class CyclistControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CyclistService cyclistService;

    @MockitoBean
    private TeamRepository teamRepository;

    //test para getall
    @Test
    public void testGetAllWithinParametersReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(2, "Vingegaard", "escalador", "Dinamarca", 87, LocalDate.now(), 65),
                new CyclistOutDto(3, "Romeo", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("", "", "")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(3, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
    }

    @Test
    public void testGetAllByNameReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("Pogaçar", "", "")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("name", "Pogaçar")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
    }

    //TODO testGetAllBySpecialtyReturnOK
    //TODO testGetAllByBirthplaceReturnOk
    //TODO testGetAllByNameAndSpecialtyReturnOk
    //TODO testGetAllByNameAndBirthplaceReturnOk
    //TODO testGetAllBySpecialtyAndBirthplaceReturnOk
    //TODO testGetAllByNameAndSpecialtyAndBirthplace
    //test para get
   @Test

    public void testGetCyclistReturnOk () throws Exception{
        Cyclist mockCyclist = new Cyclist(1,"Pogaçar", "escalador", "Eslovenia", 104, 67, LocalDate.now(), true, null);

        when(cyclistService.get(1)).thenReturn(mockCyclist);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists/{cyclistId}", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        Cyclist cyclistResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistResponse);
        assertEquals("Pogaçar", cyclistResponse.getName());
        assertEquals("escalador", cyclistResponse.getSpecialty());
    }

    @Test
    public void testGetCyclistNotFound() throws Exception {
        when(cyclistService.get(1)).thenThrow(new CyclistNotFoundException());

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists/{cyclistId}", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getCode());
        assertEquals("The cyclist does not exist", errorResponse.getMessage());
    }

    //test de validaciones
    @Test
    public void testAddCyclistCreated() throws Exception {
        String name = "Pogaçar";
        String specialty = "escalador";
        String birthplace = "Eslovenia";
        int titles = 104;
        long teamId = 98;

        CyclistOutDto cyclistOutDto = new CyclistOutDto(1, name, specialty, birthplace, titles, LocalDate.now(), teamId);
        CyclistRegistrationDto cyclistRegistrationDto = new CyclistRegistrationDto(name, specialty, birthplace, titles, 58, LocalDate.now(), true, teamId);
        when(cyclistService.add(teamId, cyclistRegistrationDto)).thenReturn(cyclistOutDto);
        String requestBody = objectMapper.writeValueAsString(cyclistRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/cyclists", teamId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialty").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthplace").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.titles").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        CyclistOutDto responseCyclistOutDto = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(responseCyclistOutDto);
        assertEquals(name, responseCyclistOutDto.getName());
        assertEquals(specialty, responseCyclistOutDto.getSpecialty());
        assertEquals(birthplace, responseCyclistOutDto.getBirthplace());
        assertEquals(titles, responseCyclistOutDto.getTitles());
        assertEquals(teamId, responseCyclistOutDto.getTeamId());
    }

    @Test
    public void testAddCyclistTeamNotFound() throws Exception {
        String name = "Pogaçar";
        String specialty = "escalador";
        String birthplace = "Eslovenia";
        int titles = 104;
        long teamId = 98;

        CyclistRegistrationDto cyclistRegistrationDto = new CyclistRegistrationDto(name, specialty, birthplace, titles, 58, LocalDate.now(), true, teamId);

        when(cyclistService.add(teamId, cyclistRegistrationDto)).thenThrow(new TeamNotFoundException());

        String requestBody = objectMapper.writeValueAsString(cyclistRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/cyclists", teamId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                        .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getCode());
        assertEquals("The team does not exist", errorResponse.getMessage());


    }

    @Test
    public void testAddCyclistValidationError() throws Exception{
        String name = null;
        String specialty = null;
        String birthplace = "Eslovenia";
        int titles = -104;
        long teamId = 98;

        CyclistRegistrationDto cyclistRegistrationDto = new CyclistRegistrationDto(name, specialty, birthplace, titles, 58, LocalDate.now(), true, teamId);

        String requestBody = objectMapper.writeValueAsString(cyclistRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/cyclists", teamId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
        assertNotNull(errorResponse);
        assertEquals(400, errorResponse.getCode());
        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals("El campo name es obligatorio", errorResponse.getErrorMessages().get("name"));
        assertEquals("El campo specialty es obligatorio", errorResponse.getErrorMessages().get("specialty"));
        assertEquals("must be greater than or equal to 0", errorResponse.getErrorMessages().get("titles"));

    }
 }
