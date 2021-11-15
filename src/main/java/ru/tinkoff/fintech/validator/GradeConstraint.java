package ru.tinkoff.fintech.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GradeValidator.class)
public @interface GradeConstraint {
    String message() default "Board game entity is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
