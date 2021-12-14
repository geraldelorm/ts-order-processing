package io.turntabl.tsops.ClientConnectivity.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("Order does not exist");
    }
}
