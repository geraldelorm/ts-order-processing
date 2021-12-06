package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
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
    private final AuthService authService;

    @Autowired
    MarketDataService marketDataService;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderValidationService orderValidationService;

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public List<Order> getUserOrder() {
        User user = authService.getCurrentUser();
        return user.getOrders();
    }

    public void createOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setProduct(orderDto.getProduct());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
        order.setStatus("CREATED");
        order.setUser(authService.getCurrentUser());
        orderRepository.save(order);

        orderValidationService.validateOrder(orderDto, order);
    }


}
