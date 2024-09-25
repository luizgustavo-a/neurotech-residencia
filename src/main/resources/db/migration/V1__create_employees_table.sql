CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(50) UNIQUE NOT NULL,
    contact VARCHAR(15) NOT NULL ,
    years_of_experience INTEGER NOT NULL ,
    linkedin_url VARCHAR(50) NOT NULL
);