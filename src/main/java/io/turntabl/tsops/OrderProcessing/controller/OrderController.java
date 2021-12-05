package io.turntabl.tsops.OrderProcessing.controller;

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

    @GetMapping
    public List<Order> getAllOrder(){
        return orderService.getAllOrder();
    }

    @GetMapping(path = "/{userId}")
    public List<Order> getUserOrder(@PathVariable("userId")Long userId){
        return orderService.getUserOrder(userId);
    }

    @PostMapping(path = "create/{userId}")
    public ResponseEntity<Void> createOrder(@RequestBody OrderDto orderDto, @PathVariable("userId") Long userId){
        //validate order
        //send orders to reporting service with order status
        // if its valid
        // process the order and send it to the exchange
        orderService.createOrder(orderDto, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


//    @GetMapping("/test")
//    public MarketData test(){
//        return orderService.marketDataForAProductOnExOne("IBM");
//    }
}
