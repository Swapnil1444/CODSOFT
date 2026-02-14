CREATE DATABASE examdb;
USE examdb;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO users(username,password) VALUES ('swapnil','1234');

CREATE TABLE questions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    question TEXT,
    option1 VARCHAR(100),
    option2 VARCHAR(100),
    option3 VARCHAR(100),
    option4 VARCHAR(100),
    correct_option INT
);

INSERT INTO questions(question,option1,option2,option3,option4,correct_option) VALUES
('What is JVM?','Java Variable Machine','Java Virtual Machine','Joint Virtual Method','None',2),
('Which keyword is used for inheritance?','extends','implements','inherit','super',1),
('Which package contains Scanner class?','java.io','java.util','java.lang','java.sql',2);

CREATE TABLE results (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    score INT,
    exam_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

select * from results ;

