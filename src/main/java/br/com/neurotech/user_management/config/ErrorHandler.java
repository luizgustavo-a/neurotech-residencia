package br.com.neurotech.user_management.config;

import br.com.neurotech.user_management.model.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleError404(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity handlePlanetNotFoundException(UserNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerError400(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getFieldErrors();

        List<ValidationErrorData> messages = new ArrayList<>(errors.size());
        errors.forEach(error -> {
            if(messages.stream().anyMatch(validationErrorData -> Objects.equals(validationErrorData.field(), error.getField()))) {
                ValidationErrorData data = messages.stream().filter(validationErrorData ->
                        Objects.equals(validationErrorData.field(), error.getField())
                ).findFirst().get();

                messages.remove(data);
                List<String> messageList = data.messages();
                String errorMessage = error.getDefaultMessage();
                messageList.add(errorMessage);
                messages.add(new ValidationErrorData(error.getField(), messageList));
            } else {
                messages.add(new ValidationErrorData(error));
            }
        });

        return ResponseEntity.badRequest().body(messages);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleError400(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleValidationError(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();

        List<ValidationErrorData> messages = new ArrayList<>(constraintViolations.size());

        constraintViolations.forEach(constraintViolation -> {
            if(messages.stream().anyMatch(validationErrorData -> Objects.equals(validationErrorData.field(), constraintViolation.getPropertyPath().toString()))) {
                ValidationErrorData data = messages.stream().filter(validationErrorData ->
                        Objects.equals(validationErrorData.field(), constraintViolation.getPropertyPath().toString())
                ).findFirst().get();
                messages.remove(data);

                List<String> messageList = data.messages();
                messageList.add(constraintViolation.getMessage());
                messages.add(new ValidationErrorData(constraintViolation.getPropertyPath().toString(), messageList));
            }
        });

        return ResponseEntity.badRequest().body(messages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleError500(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity handleError500(JpaSystemException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getLocalizedMessage());
    }

    private record ValidationErrorData(
            String field,
            List<String> messages
    ) {
        public ValidationErrorData(FieldError error) {
            this(error.getField(), new ArrayList<>(Collections.singletonList(error.getDefaultMessage())));
        }
    }

}
