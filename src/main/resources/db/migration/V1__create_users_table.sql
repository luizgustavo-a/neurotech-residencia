CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    contact VARCHAR(255) NOT NULL ,
    years_of_experience INTEGER NOT NULL ,
    linkedin_url VARCHAR(255) NOT NULL
);