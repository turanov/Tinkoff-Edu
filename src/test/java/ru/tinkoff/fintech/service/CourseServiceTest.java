package ru.tinkoff.fintech.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.fintech.cashe.LRUCourseCache;
import ru.tinkoff.fintech.dao.CourseRepository;
import ru.tinkoff.fintech.model.Course;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class CourseServiceTest {

    @Mock
    private LRUCourseCache LRUCourseCache;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private CourseService courseService;

    @Test
    void saveTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());

        courseService.save(course);

        verify(courseRepository, times(1)).save(course);
        verify(LRUCourseCache, times(1)).deleteById(1);
    }

    @Test
    void updateTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        courseService.save(course);
        courseService.update(course);

        verify(courseRepository, times(1)).update(course);
        verify(LRUCourseCache, times(2)).deleteById(1);
    }

    @Test
    void deleteTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        courseService.save(course);
        courseService.deleteById(course.getId());

        verify(courseRepository, times(1)).deleteById(course.getId());
        verify(LRUCourseCache, times(2)).deleteById(1);
    }

    @Test
    void findByIdTest() {
        Course course = new Course(1, "History", "History is history", 3, Collections.emptySet());
        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        Assert.assertEquals(course, courseService.findById(1));

        verify(LRUCourseCache, times(2)).findById(1);
        verify(LRUCourseCache, times(1)).save(course);
    }
}