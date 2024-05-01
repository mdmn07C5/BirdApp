CREATE TABLE user (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT UNIQUE,
    password TEXT
);

CREATE TABLE message (
    message_id INTEGER PRIMARY KEY AUTOINCREMENT,
    posted_by INTEGER,
    message_text TEXT,
    time_posted_epoch BIGINT,
    FOREIGN KEY (posted_by) REFERENCES user(user_id)
);