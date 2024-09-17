CREATE TABLE user_certification (
    user_id BIGINT NOT NULL,
    certification_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, certification_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (certification_id) REFERENCES certifications(id) ON DELETE CASCADE
);

CREATE TABLE user_technical_competence (
    user_id BIGINT NOT NULL,
    technical_competence_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, technical_competence_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (technical_competence_id) REFERENCES technical_competences(id) ON DELETE CASCADE
);
