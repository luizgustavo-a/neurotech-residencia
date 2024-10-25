package br.com.neurotech.protalent.model.exception;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException() {
        super("User not found.");
    }
}
