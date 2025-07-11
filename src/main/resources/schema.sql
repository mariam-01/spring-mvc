CREATE TABLE IF NOT EXISTS users (
                                     username   VARCHAR(50)   NOT NULL PRIMARY KEY,
    password   VARCHAR(100)  NOT NULL,
    enabled    BOOLEAN       NOT NULL
    );

CREATE TABLE IF NOT EXISTS authorities (
                                           username   VARCHAR(50)   NOT NULL,
    authority  VARCHAR(50)   NOT NULL,
    CONSTRAINT fk_auth_users FOREIGN KEY(username)
    REFERENCES users(username) ON DELETE CASCADE
    );

CREATE UNIQUE INDEX IF NOT EXISTS idx_auth_username
    ON authorities(username, authority);
