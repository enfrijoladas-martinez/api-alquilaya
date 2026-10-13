package com.api.alquilaya.exceptions;

// Se lanza cuando se rompe una regla de negocio -> HTTP 400
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
