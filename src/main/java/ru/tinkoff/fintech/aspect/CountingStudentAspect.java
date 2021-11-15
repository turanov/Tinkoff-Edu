package ru.tinkoff.fintech.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintech.model.Student;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class CountingStudentAspect {
    private Map<Student, Integer> amountOfCalls = new HashMap<>();

    public CountingStudentAspect() {
    }

    @Pointcut("@annotation(ru.tinkoff.fintech.annotation.CountStudents)")
    public void executeCounting() {
    }

    @AfterReturning(pointcut = "executeCounting()", returning = "returnValue")
    public void countMethodCall(JoinPoint joinPoint, Object returnValue) throws IOException {
        String calledMethod = joinPoint.getSignature().getName();
        if (calledMethod.equals("getBusyStudents")) {
            List<Student> busyStudents = (List<Student>) returnValue;
            for (Student student : busyStudents) {
                if (!amountOfCalls.containsKey(student)) {
                    amountOfCalls.put(student, 0);
                }
                amountOfCalls.put(student, amountOfCalls.get(student) + 1);
            }
        }
    }
}
