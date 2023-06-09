package ru.vzotov.accounting.interfaces.contacts.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.vzotov.accounting.interfaces.contacts.facade.ContactNotFoundException;

@ControllerAdvice
public class ContactsExceptionHandler {

    @ExceptionHandler(value = {ContactNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
