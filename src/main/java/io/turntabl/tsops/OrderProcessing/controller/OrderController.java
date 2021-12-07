package io.turntabl.tsops.OrderProcessing.controller;

import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrder(){
        return orderService.getAllOrder();
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<Order>> getUserOrder(){
        return orderService.getUserOrder();
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto){
        if(authService.isClient()){
            orderService.createOrder(orderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        else return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
}
