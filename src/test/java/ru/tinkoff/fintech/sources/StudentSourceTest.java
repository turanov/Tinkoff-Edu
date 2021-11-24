package ru.tinkoff.fintech.sources;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.tinkoff.fintech.model.Student;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class StudentSourceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void findByIdByByRoleUserTest() {

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void findByIdByByRoleAdminTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void saveByRoleUserTest() {
        Student student = new Student(4, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void saveByRoleAdminTest() {
        Student student = new Student(4, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(authenticated())
                .andExpect(status().isCreated());
    }


    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateByRoleUserTest() {

        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void updateByRoleAdminTest() {

        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }


    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void deleteByRoleUserTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", 1))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void deleteByRoleAdminTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", 1))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
    }
}
