package io.turntabl.tsops.OrderProcessing.controller;

import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
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
    public ResponseEntity<List<Order>> getAllOrder(){
        if(authService.isAdmin()) return new ResponseEntity<>(orderService.getAllOrder(), HttpStatus.OK) ;
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<Order>> getUserOrder(){
        if(authService.isClient()) return new ResponseEntity<>(orderService.getUserOrder(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto){
        if(authService.isClient()){
            orderService.createOrder(orderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PutMapping(path = "/create")
    public ResponseEntity<Void> updateOrder(@RequestBody OrderDto orderDto){
        if(authService.isClient()){
            orderService.updateOrder(orderDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @DeleteMapping(path = "/delete/{orderID}")
    public ResponseEntity<Void> deleteOrder(@RequestParam int orderID){
        if(authService.isClient()){
            orderService.deleteOrder(orderID);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    //TODO
    // Get initial market Data even if marketDataService is not up.

}
