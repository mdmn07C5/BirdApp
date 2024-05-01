create table account (
    account_id int primary key auto_increment,
    username varchar(20) unique,
    password varchar(255)
);