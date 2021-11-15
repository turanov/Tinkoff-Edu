package ru.tinkoff.fintech;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintech.model.Student;
import ru.tinkoff.fintech.service.StudentService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {
    private final StudentService studentService;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public ScheduledTasks(StudentService studentService) {
        this.studentService = studentService;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void reportCurrentTime() {
        List<Student> students = studentService.findAllBusyStudents();
        System.out.println("The time is now " + dateFormat.format(new Date()));
        System.out.println("Busy students");
        students.forEach(System.out::println);
    }
}
