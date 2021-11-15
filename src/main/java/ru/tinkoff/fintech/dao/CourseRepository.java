package ru.tinkoff.fintech.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.tinkoff.fintech.model.Course;
import ru.tinkoff.fintech.model.Student;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CourseRepository {

    void save(Course course);

    List<Course> findAll();

    Optional<Course> findById(int id);

    void deleteById(int id);

    void deleteAll();

    void update(Course course);

    Optional<Course> findTheCourseWithTheHighestAverageAgeOfStudents();

    void addStudent(int id, Student student);

}
