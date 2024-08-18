package ru.kata.spring.boot_security.demo.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ValidationHandler {
    UserValidator userValidator;

    @Autowired
    void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        userValidator.validate((String) e.getFieldValue("username"), Long.parseLong((String) Objects.requireNonNull(e.getFieldValue("id"))), bindingResult);
        Map<String, String> errors = new HashMap<>();
        List<ObjectError> allErrors = bindingResult.getAllErrors();
        allErrors.forEach(err -> {
            FieldError fieldError = (FieldError) err;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return errors;
    }
}
