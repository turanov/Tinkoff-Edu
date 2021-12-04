package ru.tinkoff.fintech.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintech.kafka.Producer;
import ru.tinkoff.fintech.model.Student;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Aspect
public class CountingStudentAspect {
    private Map<Student, Integer> amountOfCalls = new HashMap<>();
    private final Producer producer;

    public CountingStudentAspect(Producer producer) {
        this.producer = producer;
    }

    @Pointcut("@annotation(ru.tinkoff.fintech.annotation.CountStudents)")
    public void executeCounting() {
    }

    @Pointcut("@annotation(ru.tinkoff.fintech.annotation.ConditionStudent)")
    public void executeSaving() {
    }

    @AfterReturning(pointcut = "executeCounting()", returning = "returnValue")
    public void countMethodCall(JoinPoint joinPoint, Object returnValue) throws IOException {
        String calledMethod = joinPoint.getSignature().getName();
        if (calledMethod.equals("findAllBusyStudents")) {
            List<Student> busyStudents = (List<Student>) returnValue;
            for (Student student : busyStudents) {
                if (!amountOfCalls.containsKey(student)) {
                    amountOfCalls.put(student, 0);
                }
                amountOfCalls.put(student, amountOfCalls.get(student) + 1);
            }
        }
    }

    @AfterReturning(pointcut = "executeSaving()", returning = "returnValue")
    public void saveMethodCall(JoinPoint joinPoint, Object returnValue) throws IOException {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (signature.getName().equals("save")) {
            for (Object object : joinPoint.getArgs()) {
                Student student = (Student) object;
                producer.send(student);
            }
        } else if (signature.getName().equals("deleteById")) {
            Student student = (Student) returnValue;
            producer.send(student);
        } else if (signature.getName().equals("deleteAll")) {
            List<Student> students = (List<Student>) returnValue;
            for (Student student : students) {
                producer.send(student);
            }
        } else if (signature.getName().equals("update")) {
            for (Object object : joinPoint.getArgs()) {
                Student student = (Student) object;
                producer.send(student);
            }
        }
    }
}
