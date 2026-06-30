package org.example.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception{
    private HttpStatus code;

    public UserException(HttpStatus status, String message){
        super(message);
        this.code = code;
    }

    public HttpStatus getCode() {
        return code;
    }

}
