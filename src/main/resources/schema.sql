drop table if exists post;
drop table if exists account;

create table account (
    account_id int primary key auto_increment,
    username varchar(20) unique,
    password varchar(255)
);

create table post (
    post_id int primary key auto_increment,
    posted_by int,
    post_content varchar(140),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)
);


insert into account (username, password) values ('apu_apustaja', 'password');
insert into account (username, password) values ('spurdo_spadre', 'password');

insert into post (posted_by, post_content, time_posted_epoch) values (1,'test message from Apu',1714600414);
insert into post (posted_by, post_content, time_posted_epoch) values (2,'test message from Spurdo',1714600416);



-- SQLite syntax LMAO
-- CREATE TABLE user (
--     user_id INTEGER PRIMARY KEY AUTOINCREMENT,
--     username TEXT UNIQUE,
--     password TEXT
-- );

-- CREATE TABLE message (
--     message_id INTEGER PRIMARY KEY AUTOINCREMENT,
--     posted_by INTEGER,
--     message_text TEXT,
--     time_posted_epoch BIGINT,
--     FOREIGN KEY (posted_by) REFERENCES user(user_id)
-- );