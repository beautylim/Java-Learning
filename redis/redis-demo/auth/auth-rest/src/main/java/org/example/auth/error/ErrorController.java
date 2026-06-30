package org.example.auth.error;

import org.example.auth.exception.AuthException;
import org.example.auth.model.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException e) {
        return ResponseEntity.status(e.getStatus()).body(ErrorResponse.builder().message(e.getMessage()).build());
    }
}
