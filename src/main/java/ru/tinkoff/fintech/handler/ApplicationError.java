package ru.tinkoff.fintech.handler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

@Getter
public enum ApplicationError {
    STUDENT_GRADE_LESS_THAN_EXPECTED("Student's grade is less than required grade", 400),
    COURSE_NOT_FOUND("Course not found", 404),
    STUDENT_NOT_FOUND("Student not found", 404);

    private final String message;
    private final int code;

    ApplicationError(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ApplicationException exception(String args) {
        return new ApplicationException(this, args);
    }

    public static class ApplicationException extends RuntimeException {
        public final ApplicationExceptionCompanion companion;

        ApplicationException(ApplicationError error, String message) {
            super(error.message + " : " + message);
            this.companion = new ApplicationExceptionCompanion(error.code, error.message);
        }

        public static record ApplicationExceptionCompanion(@JsonIgnore int code, String message) {
        }
    }
}
