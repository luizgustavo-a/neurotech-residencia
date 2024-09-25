package br.com.neurotech.employees_management.model.exception;

public class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException() {
        super("User not found.");
    }
}
