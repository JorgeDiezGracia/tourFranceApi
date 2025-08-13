package com.svalero.tourfrance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.tourfrance.controller.CyclistController;
import com.svalero.tourfrance.domain.Cyclist;
import com.svalero.tourfrance.domain.dto.CyclistInDto;
import com.svalero.tourfrance.domain.dto.CyclistOutDto;
import com.svalero.tourfrance.domain.dto.CyclistRegistrationDto;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.exception.CyclistNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.CyclistRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @MockitoBean
    private CyclistRepository cyclistRepository;


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
    @Test
    public void testGetAllBySpecialtyReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "sprinter", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("", "escalador", "")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("specialty", "escalador")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistListResponse.getFirst().getBirthplace());
    }

    //TODO testGetAllByBirthplaceReturnOk
    @Test
    public void testGetAllByBirthplaceReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "sprinter", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("", "", "Eslovenia")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("birthplace", "Eslovenia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistListResponse.getFirst().getBirthplace());
    }
    //TODO testGetAllByNameAndSpecialtyReturnOk
    @Test
    public void testGetAllByNameAndSpecialtyReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("Pogaçar", "escalador", "")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("name", "Pogaçar")
                        .queryParam("specialty", "escalador")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistListResponse.getFirst().getBirthplace());
    }
    //TODO testGetAllByNameAndBirthplaceReturnOk
    @Test
    public void testGetAllByNameAndBirthplaceReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("Pogaçar", "", "Francia")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("name", "Pogaçar")
                        .queryParam("birthplace", "Francia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistListResponse.getFirst().getBirthplace());
    }
    //TODO testGetAllBySpecialtyAndBirthplaceReturnOk
    @Test
    public void testGetAllBySpecialtyAndBirthplaceReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("", "escalador", "Eslovenia")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("specialty", "escalador")
                        .queryParam("birthplace", "Eslovenia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistListResponse.getFirst().getBirthplace());
    }
    //TODO testGetAllByNameAndSpecialtyAndBirthplace
    @Test
    public void testGetAllByNameAndSpecialtyAndBirthplaceReturnOk() throws Exception {
        List<CyclistOutDto> mockCyclistDtoList = List.of(
                new CyclistOutDto(1, "Pogaçar", "escalador", "Eslovenia", 104, LocalDate.now(), 98),
                new CyclistOutDto(3, "Pogaçar", "escalador", "España", 104, LocalDate.now(), 1)
        );

        when(cyclistService.getAll("Pogaçar", "escalador", "Eslovenia")).thenReturn(mockCyclistDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/cyclists")
                        .queryParam("name", "Pogaçar")
                        .queryParam("specialty", "escalador")
                        .queryParam("birthplace", "Eslovenia")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<CyclistOutDto> cyclistListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(cyclistListResponse);
        assertEquals(2, cyclistListResponse.size());
        assertEquals("Pogaçar", cyclistListResponse.getFirst().getName());
        assertEquals("escalador", cyclistListResponse.getFirst().getSpecialty());
        assertEquals("Eslovenia", cyclistListResponse.getFirst().getBirthplace());
    }
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
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.teamId").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.specialty").exists())
                .andExpect(jsonPath("$.birthplace").exists())
                .andExpect(jsonPath("$.titles").exists())
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


    //TODO REMOVE(204 Y 404) Y MODIFY(200, 400, 404) (EN CLASES ONETOOMANY SÓLO 200 Y 400)

    @Test
    public void testRemoveCyclistNoContent() throws Exception {
        long cyclistId = 1L;

        // Simulamos que el servicio no lanza excepción al eliminar
        doNothing().when(cyclistService).remove(cyclistId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cyclists/{cyclistId}", cyclistId))
                .andExpect(status().isNoContent());

        verify(cyclistService, times(1)).remove(cyclistId);
    }

    @Test
    public void testRemoveCyclistNotFound() throws Exception {
        long cyclistId = 999L;

        // Simulamos que el servicio lanza CyclistNotFoundException
        doThrow(new CyclistNotFoundException()).when(cyclistService).remove(cyclistId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cyclists/{cyclistId}", cyclistId))
                .andExpect(status().isNotFound());

        verify(cyclistService, times(1)).remove(cyclistId);
    }

    @Test
    public void testModifyCyclistOk() throws Exception {
        long cyclistId = 1L;
        long teamId = 10L;

        CyclistInDto cyclistInDto = new CyclistInDto(
                "Juan Pérez",           // name
                "Sprinter",             // specialty
                "Madrid",               // birthplace
                2,                      // titles
                LocalDate.of(1990, 5, 1), // birthdate
                teamId                  // teamId
        );

        CyclistOutDto modifiedCyclist = new CyclistOutDto(
                cyclistId, "Juan Pérez", "Sprinter", "Madrid", 2,
                LocalDate.of(1990, 5, 1), teamId
        );

        when(cyclistService.modify(eq(cyclistId), any(CyclistInDto.class)))
                .thenReturn(modifiedCyclist);

        String requestBody = objectMapper.writeValueAsString(cyclistInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/cyclists/{cyclistId}", cyclistId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(cyclistId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Juan Pérez"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.specialty").value("Sprinter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthplace").value("Madrid"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.titles").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamId").value(teamId));

        verify(cyclistService, times(1)).modify(eq(cyclistId), any(CyclistInDto.class));
    }

    @Test
    public void testModifyCyclistNotFound() throws Exception {
        long cyclistId = 999L;
        long teamId = 10L;

        CyclistInDto cyclistInDto = new CyclistInDto(
                "Juan Pérez",
                "Sprinter",
                "Madrid",
                2,
                LocalDate.of(1990, 5, 1),
                teamId
        );

        // El servicio lanza la excepción
        when(cyclistService.modify(eq(cyclistId), any(CyclistInDto.class)))
                .thenThrow(new CyclistNotFoundException());

        String requestBody = objectMapper.writeValueAsString(cyclistInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/cyclists/{cyclistId}", cyclistId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(cyclistService, times(1)).modify(eq(cyclistId), any(CyclistInDto.class));
    }
    @Test
    public void testModifyTeamNotFound() throws Exception {
        long cyclistId = 1L;
        long nonexistentTeamId = 9999L;

        CyclistInDto cyclistInDto = new CyclistInDto(
                "Juan Pérez",
                "Sprinter",
                "Madrid",
                2,
                LocalDate.of(1990, 5, 1),
                nonexistentTeamId
        );

        // El servicio lanza la excepción
        when(cyclistService.modify(eq(cyclistId), any(CyclistInDto.class)))
                .thenThrow(new TeamNotFoundException());

        String requestBody = objectMapper.writeValueAsString(cyclistInDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/cyclists/{cyclistId}", cyclistId)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(cyclistService, times(1)).modify(eq(cyclistId), any(CyclistInDto.class));
    }
 }
