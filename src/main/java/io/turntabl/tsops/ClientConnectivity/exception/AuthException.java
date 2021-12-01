package io.turntabl.tsops.ClientConnectivity.exception;

public class AuthException extends RuntimeException {
    public AuthException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public AuthException(String exMessage) {
        super(exMessage);
    }
}
