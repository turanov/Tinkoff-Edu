package ru.tinkoff.fintech.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.fintech.handler.ApplicationError;
import ru.tinkoff.fintech.model.Student;
import ru.tinkoff.fintech.service.StudentService;

import javax.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> get(@PathVariable int id) {
        return new ResponseEntity<>(studentService.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public HttpEntity<Student> create(@RequestBody @Valid Student student) {
        studentService.save(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }

    @PutMapping
    public HttpEntity<Student> update(@RequestBody @Valid Student student) {
        studentService.update(student);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public HttpEntity delete(@PathVariable int id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ApplicationError.ApplicationException.class)
    public ResponseEntity<ApplicationError.ApplicationException.ApplicationExceptionCompanion> handleApplicationException(ApplicationError.ApplicationException e) {
        return ResponseEntity.status(e.companion.code()).body(e.companion);
    }
}
