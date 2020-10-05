package com.nonstech.katacalc.exceptions;

public class NegativeNumberException extends RuntimeException {

    private String message;

    public NegativeNumberException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
