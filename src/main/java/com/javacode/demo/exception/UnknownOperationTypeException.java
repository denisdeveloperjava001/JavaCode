package com.javacode.demo.exception;

public class UnknownOperationTypeException extends RuntimeException {
    public UnknownOperationTypeException() {
        super("Unknown operation type");
    }
}
