package io.turntabl.tsops.OrderProcessing.controller;

import io.turntabl.tsops.ClientConnectivity.exception.OrderNotFoundException;
import io.turntabl.tsops.ClientConnectivity.exception.OrderNotOpenException;
import io.turntabl.tsops.ClientConnectivity.exception.ResponseHandler;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    @Autowired
    private final OrderService orderService;

    @Autowired
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<Object> getAllOrder() {
        if (!authService.isAdmin())
            return ResponseHandler
                    .builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Not authorized")
                    .build();

        return ResponseHandler
                .builder()
                .status(HttpStatus.OK)
                .data(orderService.getAllOrder())
                .build();
    }

    @GetMapping(path = "/user")
    public ResponseEntity<Object> getUserOrder() {
        List<Order> userOrder = orderService.getUserOrder();

        if (userOrder.isEmpty())
            return ResponseHandler
                    .builder()
                    .status(HttpStatus.OK)
                    .message("There are no orders created")
                    .build();

        return ResponseHandler
                .builder()
                .status(HttpStatus.OK)
                .data(userOrder)
                .build();
    }

    @PostMapping(path = "/create/{portfolioID}")
    public ResponseEntity<Object> createOrder(@PathVariable Long portfolioID, @RequestBody OrderDto orderDto) {
        if (!authService.isClient())
            return ResponseHandler.builder().status(HttpStatus.FORBIDDEN).message("Not authorized").build();

        orderService.createOrder(orderDto, portfolioID);
        return ResponseHandler
                .builder()
                .status(HttpStatus.CREATED)
                .message("Order is created")
                .build();
    }

    @PutMapping(path = "/update/{orderID}")
    public ResponseEntity<Object> updateOrder(@PathVariable Long orderID, @RequestBody OrderDto newOrderDto) {
        if (!authService.isClient())
            return ResponseHandler
                    .builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Not authorized")
                    .build();

        try {
            orderService.updateOrder(newOrderDto, orderID);
        } catch (OrderNotFoundException e) {
            throw new OrderNotFoundException();
        } catch (OrderNotOpenException e) {
            throw new OrderNotOpenException();
        }

        return ResponseHandler
                .builder()
                .status(HttpStatus.ACCEPTED)
                .message("Order is updated")
                .build();
    }

    @DeleteMapping(path = "/delete/{orderID}")
    public ResponseEntity<Object> deleteOrder(@PathVariable Long orderID) {
        if (!authService.isClient())
            return ResponseHandler
                    .builder()
                    .status(HttpStatus.FORBIDDEN)
                    .message("Not authorized")
                    .build();

        try {
            orderService.deleteOrder(orderID);
        } catch (OrderNotFoundException e) {
            throw new OrderNotFoundException();
        } catch (OrderNotOpenException e) {
            throw new OrderNotOpenException();
        }

        return ResponseHandler
                .builder()
                .status(HttpStatus.ACCEPTED)
                .message("Order is deleted")
                .build();
    }

    //TODO
    // Get initial market Data even if marketDataService is not up.
}
