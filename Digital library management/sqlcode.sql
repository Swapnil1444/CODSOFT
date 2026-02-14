CREATE DATABASE librarydb;
USE librarydb;

CREATE TABLE admins (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50),
    password VARCHAR(50)
);

INSERT INTO admins(username,password) VALUES ('admin','1234');

CREATE TABLE books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100),
    author VARCHAR(100),
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE issued_books (
    id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    issued_to VARCHAR(100),
    issue_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    return_date TIMESTAMP NULL
);
SHOW TABLES;


select * from books;
