package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;

    @Autowired
    MarketDataService marketDataService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderValidationService orderValidationService;

    public ResponseEntity<List<Order>> getAllOrder() {
        if(authService.isAdmin()){
            return new ResponseEntity<>(orderRepository.findAll(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<List<Order>> getUserOrder() {
        User user = authService.getCurrentUser();
        if(authService.isClient()){
            return new ResponseEntity<>(user.getOrders(), HttpStatus.OK);
        }
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    public void createOrder(OrderDto orderDto) {
        Order order = new Order();
        User user = authService.getCurrentUser();
        order.setProduct(orderDto.getProduct());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
        order.setStatus("CREATED");
        order.setUser(user);
        orderRepository.save(order);

        orderValidationService.validateOrder(orderDto, order);
    }

    //TODO
    public void updateOrder(){

    }

    //TODO
    public void deleteOrder(){

    }

}
