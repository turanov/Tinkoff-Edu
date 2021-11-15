package ru.tinkoff.fintech.dao;

import org.apache.ibatis.annotations.Mapper;
import ru.tinkoff.fintech.model.Student;

import java.util.List;
import java.util.Optional;

@Mapper
public interface StudentRepository {

    void save(Student student);

    List<Student> findAll();

    Optional<Student> findById(int id);

    void deleteById(int id);

    void deleteAll();

    void update(Student student);

    List<Student> findAllBusyStudents();

    List<Student> findByName(String name);

}
