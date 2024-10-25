package br.com.neurotech.employees_management.model.exception;

public class TechnicalCompetenceNotFoundException extends Exception {
    public TechnicalCompetenceNotFoundException() {
        super("Technical Competence not found.");
    }
}
