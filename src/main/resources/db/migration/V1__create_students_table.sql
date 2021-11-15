CREATE TABLE students
(
    id        INT PRIMARY KEY,
    name      VARCHAR(64) NOT NULL,
    age       INT         NOT NULL,
    time_from INT         NOT NULL,
    time_to   INT         NOT NULL,
    grade     INT         NOT NULL
);