package ru.tinkoff.fintech.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {
    @NotNull
    private Integer id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private int required_grade;

    private Set<Student> students;
}
