CREATE TABLE courses
(
    id            INT PRIMARY KEY,
    title         VARCHAR(64)  NOT NULL,
    description   VARCHAR(256) NOT NULL,
    required_grade INT          NOT NULL
);