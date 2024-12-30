package com.javacode.demo.exception;

public class NotEnoughFundsException extends RuntimeException {
    public NotEnoughFundsException() {
        super("Not enough funds");
    }
}
