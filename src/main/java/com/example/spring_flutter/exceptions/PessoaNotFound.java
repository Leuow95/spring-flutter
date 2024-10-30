package com.example.spring_flutter.exceptions;

public class PessoaNotFound extends RuntimeException{
    public PessoaNotFound(String message) {
        super(message);
    }
}
