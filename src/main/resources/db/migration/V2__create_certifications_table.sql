CREATE TABLE certifications (
    id SERIAL PRIMARY KEY,
    description VARCHAR(100) NOT NULL,
    institution VARCHAR(50) NOT NULL,
    date DATE NOT NULL,
    hours INTEGER NOT NULL
);
