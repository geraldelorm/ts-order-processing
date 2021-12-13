package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderStatus;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final AuthService authService;

    @Autowired
    MarketDataService marketDataService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrderValidationService orderValidationService;

    @Autowired
    ExchangeConnectivityService exchangeConnectivityService;

    public List<Order> getAllOrder() {
       return orderRepository.findAll();
    }

    public List<Order> getUserOrder() {
        User user = authService.getCurrentUser();
        return user.getOrders();
    }

    public void createOrder(OrderDto orderDto) {
        Order order = new Order();
        User user = authService.getCurrentUser();
        order.setProduct(orderDto.getProduct());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
        order.setStatus(OrderStatus.CREATED);
        order.setUser(user);
        orderRepository.save(order);

        orderValidationService.validateOrder(orderDto, order);
    }

    //TODO
    public void updateOrder(OrderDto newOrderDto, Long orderID){
        Order order = orderRepository.getById(orderID);

        if(order.getStatus().equals(OrderStatus.IN_PROGRESS) || order.getStatus().equals(OrderStatus.PENDING)) {
            exchangeConnectivityService.updateOrderOnExchange(newOrderDto, order);
        } else {
            log.info("Order you want to cancel is not open");
        }
    }

    //TODO
    public void deleteOrder(Long orderID){
        Order order = orderRepository.getById(orderID);

        if(order.getStatus().equals(OrderStatus.IN_PROGRESS) || order.getStatus().equals(OrderStatus.PENDING)) {
            exchangeConnectivityService.cancelOrderOnExchange(order);
        } else {
            log.info("Order you want to cancel is not open");
        }
    }

}
