package com.project3.storemanagement.exception;

public class UniqueKeyConstraintException extends RuntimeException {
    public UniqueKeyConstraintException(String message) {
        super(message);
    }
}
