package com.api.alquilaya.exceptions;

// Se lanza cuando un recurso no existe -> HTTP 404
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
