package ru.tinkoff.fintech.validator;

import ru.tinkoff.fintech.model.Course;
import ru.tinkoff.fintech.model.Student;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GradeValidator implements ConstraintValidator<GradeConstraint, Student> {
    @Override
    public boolean isValid(Student student, ConstraintValidatorContext constraintValidatorContext) {
        if (student.getCourses() != null) {
            for (Course course : student.getCourses()) {
                if (student.getGrade() < course.getRequired_grade())
                    return false;
            }
        }
        return true;
    }
}
