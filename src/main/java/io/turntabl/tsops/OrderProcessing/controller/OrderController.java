package io.turntabl.tsops.OrderProcessing.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.entity.MarketData;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.service.MarketDataService;
import io.turntabl.tsops.OrderProcessing.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
public class OrderController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MarketDataService marketDataService;

    private final OrderService orderService;
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




//    @PostConstruct
//    public void sendInitialData() throws JsonProcessingException {
//        MarketData[] initialMarketDataFromExchange1 = restTemplate.getForObject("https://exchange.matraining.com/md", MarketData[].class);
//        MarketData[] initialMarketDataFromExchange2 = restTemplate.getForObject("https://exchange2.matraining.com/md", MarketData[].class);
//
//        if (initialMarketDataFromExchange1 != null) {
//            marketDataService.marketDataFromExOne(Arrays.asList(initialMarketDataFromExchange1).toString());
//        }
//
//        if (initialMarketDataFromExchange2 != null) {
//            marketDataService.marketDataFromExTwo(Arrays.asList(initialMarketDataFromExchange2).toString());
//        }
//    }
}
