CREATE DATABASE atmdb;
USE atmdb;

CREATE TABLE accounts (
    id INT PRIMARY KEY AUTO_INCREMENT,
    userid VARCHAR(50),
    pin VARCHAR(10),
    balance DOUBLE
);

CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    userid VARCHAR(50),
    type VARCHAR(50),
    amount DOUBLE,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO accounts(userid,pin,balance) VALUES ('swapnil','1234',5000);

SELECT * FROM accounts;
SELECT * FROM transactions; 

