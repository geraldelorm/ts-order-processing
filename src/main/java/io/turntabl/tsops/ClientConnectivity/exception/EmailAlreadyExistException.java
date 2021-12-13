package io.turntabl.tsops.ClientConnectivity.exception;

public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException() {
        super("Email already registered!");
    }
}
