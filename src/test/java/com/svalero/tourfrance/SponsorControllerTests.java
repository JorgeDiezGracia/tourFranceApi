package com.svalero.tourfrance;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.svalero.tourfrance.controller.SponsorController;
import com.svalero.tourfrance.domain.dto.ErrorResponse;
import com.svalero.tourfrance.domain.dto.SponsorOutDto;
import com.svalero.tourfrance.domain.dto.SponsorRegistrationDto;
import com.svalero.tourfrance.exception.SponsorNotFoundException;
import com.svalero.tourfrance.exception.TeamNotFoundException;
import com.svalero.tourfrance.repository.SponsorRepository;
import com.svalero.tourfrance.service.SponsorService;
import com.svalero.tourfrance.domain.dto.SponsorInDto;
import com.svalero.tourfrance.domain.Sponsor;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SponsorController.class)
public class SponsorControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SponsorService sponsorService;

    @MockitoBean
    private SponsorRepository sponsorRepository;

    //tests para getAll


    @Test
    public void testGetAllWithinParametersReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65),
                new SponsorOutDto(3, "Movistar", "España", "movistar@movistar.com", 3500, LocalDate.now(), 1)
        );

        when(sponsorService.getAll("", "", "")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(3, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
    }


    @Test
    public void testGetAllByNameReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("UAD", "", "")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("name", "UAD")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
    }

    //TODO testGetAllByCountryReturnOK
    @Test
    public void testGetAllByCountryReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("", "Emiratos Arabes", "")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("country", "Emiratos Arabes")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
        assertEquals("uad@uad.com", sponsorListResponse.getFirst().getEmail());
    }
    @Test
    public void testGetAllByEmailReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("", "", "uad@uad.com")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("email", "uad@uad.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
        assertEquals("uad@uad.com", sponsorListResponse.getFirst().getEmail());
    }
    @Test
    public void testGetAllByNameAndCountryReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("UAD", "Emiratos Arabes", "")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("name", "UAD")
                        .queryParam("country", "Emiratos Arabes")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
        assertEquals("uad@uad.com", sponsorListResponse.getFirst().getEmail());
    }

    @Test
    public void testGetAllByNameAndEmailReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("UAD", "", "uad@uad.com")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("name", "UAD")
                        .queryParam("email", "uad@uad.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
        assertEquals("uad@uad.com", sponsorListResponse.getFirst().getEmail());
    }

    @Test
    public void testGetAllByCountryAndEmailReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("", "Emiratos Arabes", "uad@uad.com")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("country", "Emiratos Arabes")
                        .queryParam("email", "uad@uad.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
        assertEquals("uad@uad.com", sponsorListResponse.getFirst().getEmail());
    }

    @Test
    public void testGetAllByNameAndCountryAndEmailReturnOk() throws Exception {
        List<SponsorOutDto> mockSponsorDtoList = List.of(
                new SponsorOutDto(1, "UAD", "Emiratos Arabes", "uad@uad.com", 1500, LocalDate.now(), 98),
                new SponsorOutDto(2, "Lease a Bike", "Dinamarca", "leaseabike@leaseabike.com", 400, LocalDate.now(), 65)
        );

        when(sponsorService.getAll("UAD", "Emiratos Arabes", "uad@uad.com")).thenReturn(mockSponsorDtoList);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors")
                        .queryParam("name", "UAD")
                        .queryParam("country", "Emiratos Arabes")
                        .queryParam("email", "uad@uad.com")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        List<SponsorOutDto> sponsorListResponse = objectMapper.readValue(jsonResponse, new TypeReference<>() {
        });

        assertNotNull(sponsorListResponse);
        assertEquals(2, sponsorListResponse.size());
        assertEquals("UAD", sponsorListResponse.getFirst().getName());
        assertEquals("Emiratos Arabes", sponsorListResponse.getFirst().getCountry());
        assertEquals("uad@uad.com", sponsorListResponse.getFirst().getEmail());
    }






    //test para get
   @Test

    public void testGetSponsorReturnOk () throws Exception{
        Sponsor mockSponsor = new Sponsor(1,"UAD", "Emiratos Arabes", "uad@uad.com", 1500, 2004, LocalDate.now(), true, null);

        when(sponsorService.get(1)).thenReturn(mockSponsor);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors/{sponsorId}", "1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        Sponsor sponsorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(sponsorResponse);
        assertEquals("UAD", sponsorResponse.getName());
        assertEquals("Emiratos Arabes", sponsorResponse.getCountry());
    }

    @Test
    public void testGetSponsorNotFound() throws Exception {
        when(sponsorService.get(1)).thenThrow(new SponsorNotFoundException());

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get("/sponsors/{sponsorId}", "1"))
                .andExpect(status().isNotFound())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        ErrorResponse errorResponse = objectMapper.readValue(jsonResponse, new TypeReference<>(){});
        assertNotNull(errorResponse);
        assertEquals(404, errorResponse.getCode());
        assertEquals("The sponsor does not exist", errorResponse.getMessage());
    }

    //test de validaciones
    @Test
    public void testAddSponsorCreated() throws Exception {
        String name = "UAD";
        String country = "Emiratos Arabes";
        String email = "uad@uad.com";
        int employees = 104;
        float funding = 2004;
        LocalDate endContract = LocalDate.now();
        boolean mainSponsor = true;
        long teamId = 98;

        SponsorOutDto sponsorOutDto = new SponsorOutDto(1, name, country, email, employees, LocalDate.now(), teamId);
        SponsorRegistrationDto sponsorRegistrationDto = new SponsorRegistrationDto(name, country, email, employees, 2004, LocalDate.now(), true, teamId);
        when(sponsorService.add(teamId, sponsorRegistrationDto)).thenReturn(sponsorOutDto);
        String requestBody = objectMapper.writeValueAsString(sponsorRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/sponsors", teamId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees").exists())
                .andReturn();

        String jsonResponse = response.getResponse().getContentAsString();
        SponsorOutDto responseSponsorOutDto = objectMapper.readValue(jsonResponse, new TypeReference<>(){});

        assertNotNull(responseSponsorOutDto);
        assertEquals(name, responseSponsorOutDto.getName());
        assertEquals(country, responseSponsorOutDto.getCountry());
        assertEquals(email, responseSponsorOutDto.getEmail());
        assertEquals(employees, responseSponsorOutDto.getEmployees());
        assertEquals(teamId, responseSponsorOutDto.getTeamId());
    }

    @Test
    public void testAddSponsorTeamNotFound() throws Exception {
        String name = "UAD";
        String country = "Emiratos Arabes";
        String email = "uad@uad.com";
        int employees = 1500;
        long teamId = 98;

        SponsorRegistrationDto sponsorRegistrationDto = new SponsorRegistrationDto(name, country, email, employees, 58, LocalDate.now(), true, teamId);

        when(sponsorService.add(teamId, sponsorRegistrationDto)).thenThrow(new TeamNotFoundException());

        String requestBody = objectMapper.writeValueAsString(sponsorRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/sponsors", teamId)
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
    public void testAddSponsorValidationError() throws Exception{
        String name = null;
        String country = null;
        String email = "Eslovenia";
        int employees = -58;
        long teamId = 98;

        SponsorRegistrationDto sponsorRegistrationDto = new SponsorRegistrationDto(name, country, email, employees, 58, LocalDate.now(), true, teamId);

        String requestBody = objectMapper.writeValueAsString(sponsorRegistrationDto);
        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.post("/teams/{teamId}/sponsors", teamId)
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
        assertEquals("El campo country es obligatorio", errorResponse.getErrorMessages().get("country"));
        assertEquals("must be greater than or equal to 0", errorResponse.getErrorMessages().get("employees"));

    }


}
