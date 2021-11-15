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
import ru.tinkoff.fintech.model.Course;
import ru.tinkoff.fintech.model.Student;
import ru.tinkoff.fintech.service.CourseService;
import ru.tinkoff.fintech.service.StudentService;

import java.util.Collections;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.tinkoff.fintech.handler.ApplicationError.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

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
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        courseService.save(course);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/courses/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(course)));
    }

    @SneakyThrows
    @Test
    void findByIdShouldFailTest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/courses/{id}", 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(404))
                .andExpect(jsonPath("message").value(COURSE_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Test
    void saveTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(course)));
    }

    @SneakyThrows
    @Test
    void saveShouldFailTest() {
        Course course = new Course(1, null, "History is history", 3, null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void updateTest() {

        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        courseService.save(course);

        course.setTitle("Algebra");

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(course)));
    }

    @SneakyThrows
    @Test
    void updateShouldFailTest() {

        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("message").value(COURSE_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Test
    void deleteTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        courseService.save(course);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/courses/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @SneakyThrows
    @Test
    void deleteShouldFailTest() {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/courses/{id}", 1))
                .andExpect(status().is(404))
                .andExpect(jsonPath("message").value(COURSE_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Test
    void addStudentTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        studentService.save(student);
        courseService.save(course);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/1/addstudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @SneakyThrows
    @Test
    void addStudentShouldFailTest() {
        Student student = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        courseService.save(course);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/1/addstudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andExpect(jsonPath("message").value(STUDENT_NOT_FOUND.getMessage()));
    }

    @SneakyThrows
    @Test
    void addStudentsTest() {
        Student student1 = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());
        Student student2 = new Student(2, "islam", 22, 8, 15, 4, Collections.emptySet());

        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        studentService.save(student1);
        studentService.save(student2);
        courseService.save(course);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/1/addstudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Set.of(student1, student2))))
                .andExpect(status().isOk());

    }

    @SneakyThrows
    @Test
    void addStudentsShouldFailTest() {
        Student student1 = new Student(1, "islam", 22, 8, 15, 4, Collections.emptySet());
        Student student2 = new Student(2, "islam", 22, 8, 15, 3, Collections.emptySet());

        Course course = new Course(1, "History", "History is history", 4, Collections.emptySet());

        studentService.save(student1);
        studentService.save(student2);
        courseService.save(course);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/courses/1/addstudents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Set.of(student1, student2))))
                .andExpect(status().is(400))
                .andExpect(jsonPath("message").value(STUDENT_GRADE_LESS_THAN_EXPECTED.getMessage()));

    }

}