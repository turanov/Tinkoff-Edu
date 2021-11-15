package ru.tinkoff.fintech.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintech.cashe.LRUCourseCache;
import ru.tinkoff.fintech.dao.CourseRepository;
import ru.tinkoff.fintech.model.Course;
import ru.tinkoff.fintech.model.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.tinkoff.fintech.handler.ApplicationError.COURSE_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;
    private final StudentService studentService;
    private final LRUCourseCache LRUCourseCache;

    public void save(Course course) {
        repository.save(course);
        LRUCourseCache.deleteById(course.getId());
    }

    public Course findById(int id) {
        if (!hasCourse(id))
            throw COURSE_NOT_FOUND.exception("error");

        Optional<Course> course = LRUCourseCache.findById(id);

        if (course.isEmpty())
            course = repository.findById(id);
        LRUCourseCache.save(course.get());
        return course.get();
    }

    public List<Course> findAll() {
        return repository.findAll();
    }

    public void deleteById(int id) {
        if (!hasCourse(id))
            throw COURSE_NOT_FOUND.exception("error");
        LRUCourseCache.deleteById(id);
        repository.deleteById(id);

    }

    public void deleteAll() {
        repository.deleteAll();
        LRUCourseCache.deleteAll();
    }

    public void update(Course course) {
        if (!hasCourse(course.getId()))
            throw COURSE_NOT_FOUND.exception("error");
        repository.update(course);
        LRUCourseCache.deleteById(course.getId());
    }

    public Course findTheCourseWithTheHighestAverageAgeOfStudents() {
        return repository.findTheCourseWithTheHighestAverageAgeOfStudents().orElseThrow();
    }

    public void addStudent(int id, Student student) {
        if (repository.findById(id).isEmpty()) {
            throw COURSE_NOT_FOUND.exception("Error");
        }
        studentService.findById(student.getId());
        repository.addStudent(id, student);
    }

    public void addStudents(int id, Set<Student> students) {
        for (Student student : students) {
            addStudent(id, student);
        }
    }

    boolean hasCourse(int id) {
        return LRUCourseCache.findById(id).isPresent() || repository.findById(id).isPresent();
    }
}
