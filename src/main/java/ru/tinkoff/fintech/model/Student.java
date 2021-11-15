package ru.tinkoff.fintech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.fintech.validator.GradeConstraint;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GradeConstraint
public class Student implements Serializable {
    @NotNull
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private int age;
    @NotNull
    private int time_from;
    @NotNull
    private int time_to;
    @NotNull
    private int grade;

    private Set<Course> courses;
}
