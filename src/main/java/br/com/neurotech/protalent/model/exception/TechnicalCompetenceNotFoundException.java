package br.com.neurotech.protalent.model.exception;

public class TechnicalCompetenceNotFoundException extends Exception {
    public TechnicalCompetenceNotFoundException() {
        super("Technical Competence not found.");
    }
}
