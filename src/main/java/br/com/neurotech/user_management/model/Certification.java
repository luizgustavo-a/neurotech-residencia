package br.com.neurotech.user_management.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String institution;

    private LocalDate date;

    private Integer hours;

    public Certification() {
    }

    public Certification(String description, String institution, LocalDate date, Integer hours) {
        this.description = description;
        this.institution = institution;
        this.date = date;
        this.hours = hours;
    }

    public Certification(Long id, String description, String institution, LocalDate date, Integer hours) {
        this.id = id;
        this.description = description;
        this.institution = institution;
        this.date = date;
        this.hours = hours;
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

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certification that = (Certification) o;
        return Objects.equals(id, that.id) && Objects.equals(description, that.description) && Objects.equals(institution, that.institution) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, institution, date);
    }
}
