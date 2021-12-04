package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    MarketDataService marketDataService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderValidationService orderValidationService;

    //get all orders
    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    //get one user's order
    public List<Order> getUserOrder(Long userId) {
        User user = userRepository.getById(userId);
        return user.getOrders();
    }

    //create an order
    public void createOrder(OrderDto orderDto, Long userId) {
        //get the order
        //save the order with user id
        //validate the order
        //send order to reporting service
        //and send order to exchange

        Order order = new Order();
        order.setProduct(orderDto.getProduct());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
//        order.setCreated_At(); add time stamp
        order.setStatus("CREATED");
        order.setUser(userRepository.getById(userId));
        orderRepository.save(order);
        System.out.println(orderDto);

        orderValidationService.validateOrder(orderDto, order);
    }


}
