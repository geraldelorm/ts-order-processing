package io.turntabl.tsops.ClientConnectivity.exception;

public class OrderNotOpenException extends RuntimeException {
    public OrderNotOpenException() {
        super("Order is not open");
    }
}
