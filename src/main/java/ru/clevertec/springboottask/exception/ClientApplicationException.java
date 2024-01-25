package ru.clevertec.springboottask.exception;

public class ClientApplicationException extends RuntimeException{
    public ClientApplicationException(String message) {
        super(message);
    }
}
