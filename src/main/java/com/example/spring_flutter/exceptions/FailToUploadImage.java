package com.example.spring_flutter.exceptions;

public class FailToUploadImage extends RuntimeException {
    public FailToUploadImage(String message) {
        super(message);
    }
}
