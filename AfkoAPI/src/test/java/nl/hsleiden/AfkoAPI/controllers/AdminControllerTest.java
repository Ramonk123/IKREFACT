package nl.hsleiden.AfkoAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hsleiden.AfkoAPI.dao.AdminDAO;
import nl.hsleiden.AfkoAPI.dao.RoleDAO;
import nl.hsleiden.AfkoAPI.models.Admin;
import nl.hsleiden.AfkoAPI.repositories.AdminRepository;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests to check admin functionality.
 * @author Daniel Paans
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminController adminController;

    @Test
    public void shouldReturnOk200WhenCreateAdmin() throws Exception {
        //Arrange
        Admin admin = new Admin("Admin@hotmail.com", "admin", "admin");

        MvcResult result = mockMvc.perform(
                post("/api/v1/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(admin)))
                .andExpect(status().isOk())
                .andReturn();
    }
}