package org.example.shop.exception;

import org.springframework.http.HttpStatus;

public class ShopException extends RuntimeException{
    private HttpStatus status;

    public ShopException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
