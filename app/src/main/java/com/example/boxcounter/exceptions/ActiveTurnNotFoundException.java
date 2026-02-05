package com.example.boxcounter.exceptions;

public class ActiveTurnNotFoundException extends RuntimeException {
    public ActiveTurnNotFoundException(String message) {
        super(message);
    }
}
