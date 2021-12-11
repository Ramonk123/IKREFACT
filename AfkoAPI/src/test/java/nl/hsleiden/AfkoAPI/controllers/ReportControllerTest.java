package nl.hsleiden.AfkoAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hsleiden.AfkoAPI.dao.AbbreviationDAO;
import nl.hsleiden.AfkoAPI.models.Abbreviation;
import nl.hsleiden.AfkoAPI.models.AbbreviationReport;
import nl.hsleiden.AfkoAPI.models.Department;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.*;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests to check reports functionality.
 * @author Daniel Paans
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AbbreviationController abbreviationController;

    @MockBean
    private AbbreviationDAO abbreviationDAO;

    @MockBean
    private Abbreviation abbreviation;

    @MockBean
    private DepartmentController departmentController;


    @Test
    public void shouldReturnOk200WhenGetAllAbbreviationReports() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/reports/abbreviation/all"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void shouldReturnOk200WhenGetAllGameScoreReports() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/reports/gamescore/all"))
                .andExpect(status().isOk())
                .andReturn();
    }
}