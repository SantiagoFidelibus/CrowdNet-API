package com.crowdfunding.capital_connection.exception;

public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(Long id) {
        super("Account does not exist with ID: " + id);
    }
}