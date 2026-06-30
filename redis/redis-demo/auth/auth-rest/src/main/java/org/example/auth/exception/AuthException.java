package org.example.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends Exception {
    private HttpStatus status;

    public AuthException() {
    }
    public AuthException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public AuthException(HttpStatus status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
