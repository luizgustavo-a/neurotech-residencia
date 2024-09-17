package br.com.neurotech.user_management.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String institution;

    private LocalDate date;

    public Certification() {
    }

    public Certification(String description, String institution, LocalDate date) {
        this.description = description;
        this.institution = institution;
        this.date = date;
    }

    public Certification(Long id, String description, String institution, LocalDate date) {
        this.id = id;
        this.description = description;
        this.institution = institution;
        this.date = date;
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

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
