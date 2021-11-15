package ru.tinkoff.fintech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.tinkoff.fintech.model.Student;
import ru.tinkoff.fintech.service.CourseService;
import ru.tinkoff.fintech.service.StudentService;

import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tinkoff.fintech.handler.ApplicationError.STUDENT_NOT_FOUND;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        courseService.deleteAll();
        studentService.deleteAll();
    }

    @SneakyThrows
    @Test
    void findByIdTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        studentService.save(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @SneakyThrows
    @Test
    void findByIdShouldFailTest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("message").value(STUDENT_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Test
    void saveTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @SneakyThrows
    @Test
    void saveShouldFailTest() {
        Student student = new Student(1, null, 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void updateTest() {

        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        studentService.save(student);

        student.setName("Turan");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(student)));
    }

    @SneakyThrows
    @Test
    void updateShouldFailTest() {

        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("message").value(STUDENT_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());

        studentService.save(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void deleteShouldFailTest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/{id}", 1))
                .andExpect(status().is(404))
                .andExpect(jsonPath("message").value(STUDENT_NOT_FOUND.getMessage()));
    }

}