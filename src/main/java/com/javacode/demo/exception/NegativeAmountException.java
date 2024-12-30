package com.javacode.demo.exception;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException() {
        super("Amount can't be negative");
    }
}
