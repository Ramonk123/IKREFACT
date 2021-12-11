package nl.hsleiden.AfkoAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hsleiden.AfkoAPI.dao.DepartmentDAO;
import nl.hsleiden.AfkoAPI.models.GameQuestion;
import nl.hsleiden.AfkoAPI.models.GameScore;
import nl.hsleiden.AfkoAPI.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentController departmentController;
    @MockBean
    private DepartmentDAO departmentDAO;
    @MockBean
    private DepartmentRepository departmentRepository;

    private static GameQuestion gameQuestionMock;

    @Autowired
    private static ObjectMapper objectMapper;

    private static GameScore gameScoreMock;

    @BeforeAll
    static void beforeAll() {
        gameQuestionMock = new GameQuestion("abbreviationNameMock","abbreviationDefinitionMock");
        objectMapper = new ObjectMapper();
        gameScoreMock = new GameScore(UUID.randomUUID(),"testUsername",15);

    }

    @Test
    void shouldReturnOk200WhenCheckingAnswer() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameQuestionMock)))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void shouldReturnOk200WhenGetGameQuestions() throws Exception {
        String limit = "10";
        MvcResult result = mockMvc.perform(get("/api/v1/game/questions").param("id", limit))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldReturnOk200WhenPostingScoreGivenCorrectBody() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/v1/game/scoreboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameScoreMock)))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void shouldReturnOk200WhenGetScores() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/game/scoreboard"))
                .andExpect(status().isOk())
                .andReturn();
    }


    @Test
    void shouldReturnError400WhenPostingScoreGivenNoBody() throws Exception{
        MvcResult result = mockMvc.perform(post("/api/v1/game/scoreboard")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    void shouldReturnError400WhenDeletingScoreGivenNoBody() throws Exception{
        MvcResult result = mockMvc.perform(delete("/api/v1/game")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    void shouldReturnOk200WhenDeletingScoreGivenCorrectBody() throws Exception{
        MvcResult result = mockMvc.perform(delete("/api/v1/game/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gameScoreMock)))
                .andExpect(status().isOk())
                .andReturn();
    }
}