package br.com.neurotech.protalent.model.exception;

public class CertificationNotFoundException extends Exception{
    public CertificationNotFoundException() {
        super("Certification not found.");
    }
}
