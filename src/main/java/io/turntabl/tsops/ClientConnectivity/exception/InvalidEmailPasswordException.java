package io.turntabl.tsops.ClientConnectivity.exception;

public class InvalidEmailPasswordException extends RuntimeException {
    public InvalidEmailPasswordException() {
        super("Invalid email or password");
    }
}
