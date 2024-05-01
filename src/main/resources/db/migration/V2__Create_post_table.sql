create table post (
    post_id int primary key auto_increment,
    posted_by int,
    post_content varchar(140),
    time_posted_epoch bigint,
    foreign key (posted_by) references  account(account_id)
);