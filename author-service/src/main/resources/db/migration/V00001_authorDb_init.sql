CREATE TABLE author (
                        id SERIAL PRIMARY KEY,
                        firstName VARCHAR(255) NOT NULL,
                        lastName VARCHAR(255) NOT NULL,
                        birthdate DATE
);