package br.com.neurotech.protalent.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    private String contact;

    @ManyToMany
    @JoinTable(
            name = "user_technial-competence",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "technical-competence_id")
    )
    private List<TechnicalCompetence> technicalCompetences;

    @ManyToMany
    @JoinTable(
            name = "user_certification",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "certification_id")
    )
    private List<Certification> certifications;

    private Integer yearsOfExperience;

    private String linkedinUrl;

    public Employee() {
    }

    public Employee(String name, String email, String contact, List<TechnicalCompetence> technicalCompetences, List<Certification> certifications, Integer yearsOfExperience, String linkedinUrl) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.technicalCompetences = technicalCompetences;
        this.certifications = certifications;
        this.yearsOfExperience = yearsOfExperience;
        this.linkedinUrl = linkedinUrl;
    }

    public Employee(Long id, String name, String email, String contact, List<TechnicalCompetence> technicalCompetences, List<Certification> certifications, Integer yearsOfExperience, String linkedinUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.technicalCompetences = technicalCompetences;
        this.certifications = certifications;
        this.yearsOfExperience = yearsOfExperience;
        this.linkedinUrl = linkedinUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<TechnicalCompetence> getTechnicalCompetences() {
        return technicalCompetences;
    }

    public void setTechnicalCompetences(List<TechnicalCompetence> technicalCompetences) {
        this.technicalCompetences = technicalCompetences;
    }

    public List<Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<Certification> certifications) {
        this.certifications = certifications;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(name, employee.name) && Objects.equals(email, employee.email) && Objects.equals(contact, employee.contact) && Objects.equals(technicalCompetences, employee.technicalCompetences) && Objects.equals(certifications, employee.certifications) && Objects.equals(yearsOfExperience, employee.yearsOfExperience) && Objects.equals(linkedinUrl, employee.linkedinUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, contact, technicalCompetences, certifications, yearsOfExperience, linkedinUrl);
    }
}
