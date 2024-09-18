CREATE TABLE certifications (
    id SERIAL PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    institution VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    hours INTEGER NOT NULL
);
