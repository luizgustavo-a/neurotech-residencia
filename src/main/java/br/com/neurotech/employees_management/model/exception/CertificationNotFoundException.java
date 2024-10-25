package br.com.neurotech.employees_management.model.exception;

public class CertificationNotFoundException extends Exception{
    public CertificationNotFoundException() {
        super("Certification not found.");
    }
}
