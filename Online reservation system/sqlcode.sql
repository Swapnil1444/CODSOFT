CREATE DATABASE reservationdb;
USE reservationdb;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO users(username,password) VALUES ('admin','1234');
INSERT INTO users(username,password) VALUES ('swapnil','1111');



