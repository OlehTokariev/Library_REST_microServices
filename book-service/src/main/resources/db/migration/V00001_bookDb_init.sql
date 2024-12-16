CREATE TABLE book (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      genre VARCHAR(100),
                      year INTEGER,
                      author_id BIGINT NOT NULL
);