package io.turntabl.tsops.ClientConnectivity.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException() {
        super("Authentication required");
    }
}
