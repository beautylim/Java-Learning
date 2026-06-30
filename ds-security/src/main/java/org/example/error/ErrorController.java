package org.example.error;

import org.example.exception.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleException(UserException e) {
        return ResponseEntity.status(e.getCode()).body(e.getMessage());
    }
}
