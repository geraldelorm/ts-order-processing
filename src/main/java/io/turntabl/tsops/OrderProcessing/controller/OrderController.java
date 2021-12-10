package io.turntabl.tsops.OrderProcessing.controller;

import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.service.MarketDataService;
import io.turntabl.tsops.OrderProcessing.service.OrderCreationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MarketDataService marketDataService;

    private final OrderCreationService orderCreationService;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrder(){
        if(authService.isAdmin()) return new ResponseEntity<>(orderCreationService.getAllOrder(), HttpStatus.OK) ;
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<Order>> getUserOrder(){
        if(authService.isClient()) return new ResponseEntity<>(orderCreationService.getUserOrder(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto){
        if(authService.isClient()){
            orderCreationService.createOrder(orderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //TODO
    // Get initial market Data even if marketDataService is not up.

}
