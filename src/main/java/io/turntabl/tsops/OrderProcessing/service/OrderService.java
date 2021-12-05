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

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public List<Order> getUserOrder(Long userId) {
        User user = userRepository.getById(userId);
        return user.getOrders();
    }

    public void createOrder(OrderDto orderDto, Long userId) {
        Order order = new Order();
        order.setProduct(orderDto.getProduct());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
        order.setStatus("CREATED");
        order.setUser(userRepository.getById(userId));
        orderRepository.save(order);

        orderValidationService.validateOrder(orderDto, order);
    }


}
