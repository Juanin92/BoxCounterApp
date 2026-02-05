package com.example.boxcounter.exceptions;

public class InvalidTurnStateException extends RuntimeException {
    public InvalidTurnStateException(String message) {
        super(message);
    }
}
