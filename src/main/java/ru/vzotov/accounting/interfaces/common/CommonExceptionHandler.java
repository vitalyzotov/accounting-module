package ru.vzotov.accounting.interfaces.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(value = {EntityConflictException.class})
    protected ResponseEntity<Object> handleConflict() {
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

}
