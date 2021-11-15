package ru.tinkoff.fintech.cashe;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.tinkoff.fintech.model.Course;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@PropertySource("classpath:application.properties")
public class LRUCourseCache {
    @Value("${cache.course.size}")
    private int capacity;
    private List<Integer> linkedNode = new LinkedList<>();

    private ConcurrentMap<Integer, Course> coursesCache = new ConcurrentHashMap();

    public void save(Course course) {
        coursesCache.put(course.getId(), course);

        if (linkedNode.contains(course.getId())) {
            linkedNode.remove(course.getId());
        } else if (linkedNode.size() == capacity) {
            coursesCache.remove(linkedNode.remove(0));
        }
        linkedNode.add(course.getId());
    }

    public Optional<Course> findById(int id) {
        return Optional.ofNullable(coursesCache.get(id));
    }

    public void deleteById(int id) {
        coursesCache.remove(id);

        if (linkedNode.contains(id))
            linkedNode.remove(id);
    }

    public void deleteAll() {
        coursesCache.clear();
        linkedNode.clear();
    }

    public int getSize(){
        return coursesCache.size();
    }
}
