CREATE TABLE students_courses
(
    student_id int,
    course_id  int,
    CONSTRAINT student_course_pk PRIMARY KEY (student_id, course_id),
    CONSTRAINT FK_student FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    CONSTRAINT FK_course FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

CREATE INDEX age ON students (age);
CREATE INDEX title ON courses (title);

INSERT INTO students
values (1, 'Islambek', 23, 9, 18, 5),
       (2, 'Ysmanali', 18, 8, 17, 4),
       (3, 'Askarali', 13, 12, 18, 3);

INSERT INTO courses
values (1, 'Algebra', 'Algebra is a branch of mathematics that substitutes letters for numbers.', 4),
       (2, 'Algorithms', 'An algorithm is a set of instructions for solving a problem or accomplishing a task.', 4),
       (3, 'English', 'English is the first language a programmer should know', 3),
       (4, 'Hisotry', 'История - это история', 5);

INSERT INTO students_courses
values (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (2, 3),
       (3, 3);