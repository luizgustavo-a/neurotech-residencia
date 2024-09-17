package br.com.neurotech.user_management.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

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

    public User() {
    }

    public User(String name, String email, String contact, List<TechnicalCompetence> technicalCompetences, List<Certification> certifications, Integer yearsOfExperience, String linkedinUrl) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.technicalCompetences = technicalCompetences;
        this.certifications = certifications;
        this.yearsOfExperience = yearsOfExperience;
        this.linkedinUrl = linkedinUrl;
    }

    public User(Long id, String name, String email, String contact, List<TechnicalCompetence> technicalCompetences, List<Certification> certifications, Integer yearsOfExperience, String linkedinUrl) {
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
}
