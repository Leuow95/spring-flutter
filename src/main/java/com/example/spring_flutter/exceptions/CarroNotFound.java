package com.example.spring_flutter.exceptions;

public class CarroNotFound extends RuntimeException{
    public CarroNotFound(String message) {
        super(message);
    }
}