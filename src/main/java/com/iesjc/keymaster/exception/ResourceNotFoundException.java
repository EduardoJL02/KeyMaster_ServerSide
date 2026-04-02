package com.iesjc.keymaster.exception;

// Heredamos de RuntimeException para no obligar a poner "throws" en cada métod0
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}