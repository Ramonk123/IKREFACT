package nl.hsleiden.AfkoAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hsleiden.AfkoAPI.JwtUtil;
import nl.hsleiden.AfkoAPI.configurations.SecurityConfiguration;
import nl.hsleiden.AfkoAPI.dao.AbbreviationDAO;
import nl.hsleiden.AfkoAPI.dao.UserDetailsDAO;
import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.models.Department;
import nl.hsleiden.AfkoAPI.repositories.AbbreviationRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

class AbbreviationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AbbreviationController abbreviationController;

    @MockBean
    private UserDetailsDAO userDetailsDAO;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AbbreviationDAO abbreviationDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AbbreviationRepository abbreviationRepository;



    @Test
    void shouldReturnOk200WhenGetAbbreviations() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/abbreviations"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldReturnOK200WhenFindAbbreviationsByAbbreviation_name() throws Exception {
        String depID = "82ff7cf0-3bd7-11ec-93f0-60f3a14d9e33";

        String abbreviationName = "BB";
        MvcResult result = mockMvc.perform(
                get("/api/v1/abbreviations/search")
                        .param("depid", depID)
                        .param("keyword", abbreviationName))
                .andExpect(status().is(200)).andReturn();

    }
    @Test
    void shouldReturnError400WhenFindAbbreviationsByAbbreviation_nameWithoutParams() throws Exception {

        MvcResult result = mockMvc.perform(
                        get("/api/v1/abbreviations/search"))
                        .andExpect(status().is(400)).andReturn();
    }

    @Test
    void shouldReturnOk200WhenFindAbbreviationsByCategory() throws Exception {
        String category1 = "A";
        String category2 = "E";
        MvcResult result = mockMvc.perform(
                        get("/api/v1/abbreviations/search/category")
                                .param("value1", category1)
                                .param("value2", category2))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    void shouldReturnError400WhenFindAbbreviationsByCategory() throws Exception {
        String category1 = "A";
        String category2 = "E";

        MvcResult result = mockMvc.perform(
                        get("/api/v1/abbreviations/search/category"))
                .andExpect(status().is(400)).andReturn();
    }

    @Test
    void shouldReturnOk200WhenGetRecent() throws Exception{
        MvcResult result = mockMvc.perform(
                        get("/api/v1/abbreviations/recent"))
                .andExpect(status().is(200)).andReturn();
    }

    @Test
    void shouldReturnOk200WhenEditAbbreviation() throws Exception{
        String abbreviationName = "testName";
        String definition = "testDef";
        MvcResult result = mockMvc.perform(
                put("/api/v1/abbreviations/c8f7fd08-18a4-47b6-9c64-d7285e16c58c")
                        .param("abbreviationName", abbreviationName)
                        .param("definition", definition)
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    void shouldReturnOk200WhenPostAbbreviations() throws Exception {
        Set<Department> departmentSet = new HashSet<>();
        departmentSet.add(new Department("Rijksbreed"));
        Abbreviation abbreviationMock = new Abbreviation(UUID.randomUUID(),departmentSet,"testName","testDefinition",new Timestamp(Calendar.getInstance().getTime().getTime()),false);
        List<Abbreviation> abbreviationList = new ArrayList<>();
        abbreviationList.add(abbreviationMock);

        MvcResult result = mockMvc.perform(
                post("/api/v1/abbreviations/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(abbreviationList))
                        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    void shouldReturnOk200WhenPostAbbreviationsByCSV() throws Exception {
        String csvBody = "abbreviation_name,definition,departments\n" +
                "MaaktNietUit,KanVanAllesZijn,Rijksbreed;P-Direkt\n" +
                "Test1,Test1,Rijksbreed";
        MvcResult result = mockMvc.perform(
                post("/api/v1/abbreviations/CSV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(csvBody))
                        .andExpect(status().isOk())
                        .andReturn();

    }
}