package ru.tinkoff.fintech.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintech.annotation.CountStudents;
import ru.tinkoff.fintech.dao.StudentRepository;
import ru.tinkoff.fintech.model.Student;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.tinkoff.fintech.handler.ApplicationError.STUDENT_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class StudentService {


    private final StudentRepository repository;

    public void save(Student student) {
        repository.save(student);
    }

    public Student findById(int id) {
        return repository.findById(id).orElseThrow(() -> STUDENT_NOT_FOUND.exception("Error"));
    }

    public List<Student> findAll() {
        return repository.findAll();
    }

    public void deleteById(int id) {
        if (repository.findById(id).isEmpty())
            throw STUDENT_NOT_FOUND.exception("error");
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void update(Student student) {
        if (repository.findById(student.getId()).isEmpty())
            throw STUDENT_NOT_FOUND.exception("error");
        repository.update(student);
    }

    @CountStudents
    public List<Student> findAllBusyStudents() {
        return repository.findAllBusyStudents();
    }

    public int sumOfAgesByName(String name) {
        return repository.findByName(name).stream()
                .map(Student::getAge)
                .reduce(0, Integer::sum);
    }

    public Set<String> getSetsOfStudentsName() {
        return repository.findAll().stream().map(Student::getName).collect(Collectors.toSet());
    }

    public Boolean anyMatchOfStudentByAge(int age) {
        return repository.findAll().stream().anyMatch(it -> it.getAge() > age);
    }

    public Map<Integer, String> studentsToMapByIdAndName() {
        return repository.findAll().stream().collect(Collectors.toMap(Student::getId, Student::getName));
    }

    public Map<Integer, List<Student>> studentsToMapGroupingByAge() {
        return repository.findAll().stream().collect(Collectors.groupingBy(Student::getAge));
    }
}
