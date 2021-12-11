package nl.hsleiden.AfkoAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hsleiden.AfkoAPI.dao.DepartmentDAO;
import nl.hsleiden.AfkoAPI.models.Department;
import nl.hsleiden.AfkoAPI.repositories.DepartmentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentController departmentController;

    @MockBean
    private DepartmentDAO departmentDAO;

    @MockBean
    private DepartmentRepository departmentRepository;

    private static ObjectMapper mapper;
    private static Department departmentMock;
    private static ArrayList<Department> departmentArrayList;
    @BeforeAll
    static void beforeAll() {
        mapper = new ObjectMapper();
        departmentMock = new Department(UUID.randomUUID(),"Rijksbreed");
        departmentArrayList = new ArrayList<>();
        departmentArrayList.add(departmentMock);
    }

    @Test
    void shouldReturnOk200WhenGetDepartments() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/departments"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void shouldReturnOk200WhenPostDepartment() throws Exception {
        departmentArrayList.add(departmentMock);
        MvcResult result = mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(departmentArrayList)))
                .andExpect(status().isOk())
                .andReturn();
    }
    @Test
    void shouldReturnError400WhenPostDepartmentWithEmptyBody() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/departments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn();
    }

    @Test
    void shouldGetOk200WhenGetDepartmentIdByDepartmentName() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/departments/search")
                .param("departmentName", "Rijksbreed")
        ).andExpect(status().isOk()).andReturn();
    }

    @Test
    void shouldGetError400WhenGetDepartmentIdByDepartmentNameWithIncorrectParam() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/departments/search")
                .param("DepartmentName", "Rijksbreed")
        ).andExpect(status().is(400)).andReturn();
    }


}