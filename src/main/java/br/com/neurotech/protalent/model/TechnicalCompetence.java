package br.com.neurotech.protalent.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "technical_competences")
public class TechnicalCompetence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String level;

    public TechnicalCompetence() {
    }

    public TechnicalCompetence(String description, String level) {
        this.description = description;
        this.level = level;
    }

    public TechnicalCompetence(Long id, String description, String level) {
        this.id = id;
        this.description = description;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TechnicalCompetence that = (TechnicalCompetence) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(level, that.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, level);
    }
}
