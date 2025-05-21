package com.simplebanking.model; // Corrected package

public class InsufficientBalanceException extends Exception {
    public InsufficientBalanceException() {
        super("Insufficient balance");
    }
}
