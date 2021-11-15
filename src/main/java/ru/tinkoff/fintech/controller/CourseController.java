package ru.tinkoff.fintech.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.model.Course;
import ru.tinkoff.fintech.model.Student;
import ru.tinkoff.fintech.service.CourseService;
import ru.tinkoff.fintech.service.StudentService;

import javax.validation.Valid;
import java.util.Set;

import static ru.tinkoff.fintech.handler.ApplicationError.ApplicationException;
import static ru.tinkoff.fintech.handler.ApplicationError.ApplicationException.ApplicationExceptionCompanion;
import static ru.tinkoff.fintech.handler.ApplicationError.STUDENT_GRADE_LESS_THAN_EXPECTED;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    private final StudentService studentService;

    public CourseController(CourseService courseService, StudentService studentService) {
        this.courseService = courseService;
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Course> get(@PathVariable int id) {
        return new ResponseEntity<>(courseService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Course> create(@RequestBody @Valid Course course) {
        courseService.save(course);
        return new ResponseEntity<>(course, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Course> update(@RequestBody @Valid Course course) {
        courseService.update(course);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable int id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{id}/addstudent")
    public ResponseEntity addStudent(@PathVariable int id, @RequestBody @Valid Student student) {
        courseService.addStudent(id, student);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/addstudents")
    public ResponseEntity addStudents(@PathVariable int id, @RequestBody Set<@Valid Student> students) {
        Course course = courseService.findById(id);
        for (Student student : students) {
            if (student.getGrade() < course.getRequired_grade()) {
                throw STUDENT_GRADE_LESS_THAN_EXPECTED.exception("Error ");
            }
        }
        courseService.addStudents(id, students);
       return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationExceptionCompanion> handleApplicationException(ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
