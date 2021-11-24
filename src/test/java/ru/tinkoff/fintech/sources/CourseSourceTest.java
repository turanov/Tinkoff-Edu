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
import ru.tinkoff.fintech.model.Course;
import ru.tinkoff.fintech.model.Student;

import java.util.Collections;
import java.util.Set;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CourseSourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void unauthenticatedTest() {
        mockMvc.perform(get("/courses/1"))
                .andDo(print())
                .andExpect(unauthenticated());
    }

    @SneakyThrows
    @Test
    @WithMockUser("user")
    public void authenticatedTest() {
        mockMvc.perform(get("/courses/1"))
                .andExpect(authenticated());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void findByIdByRoleUserTest() {
        mockMvc.perform(get("/courses/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void findByIdByRoleAdminTest() {
        mockMvc.perform(get("/courses/1"))
                .andDo(print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void saveByRoleUserTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void saveByRoleAdminTest() {
        Course course = new Course(5, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(authenticated())
                .andExpect(status().isCreated());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void updateByRoleUserTest() {

        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void updateByRoleAdminTest() {

        Course course = new Course(4, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void deleteByRoleUserTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/courses/{id}", 1))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void deleteByRoleAdminTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/courses/{id}", 1))
                .andExpect(authenticated())
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void addStudentByRoleUserTest() {
        Student student = new Student(3, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/1/addstudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void addStudentByRoleAdminTest() {
        Student student = new Student(3, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/1/addstudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void addStudentsByRoleUserTest() {
        Student student1 = new Student(1, "Islambek", 23, 9, 18, 5, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/4/addstudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Set.of(student1))))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @SneakyThrows
    @Test
    @WithMockUser(username = "user", roles = {"ADMIN"})
    public void addStudentsByRoleAdminTest() {
        Student student1 = new Student(1, "Islambek", 23, 9, 18, 5, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/4/addstudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Set.of(student1))))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }
}
