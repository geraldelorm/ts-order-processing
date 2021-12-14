package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.exception.OrderNotFoundException;
import io.turntabl.tsops.ClientConnectivity.exception.OrderNotOpenException;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderStatus;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

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

    public void createOrder(OrderDto orderDto, Long portfolioID) {
        Order order = new Order();
        User user = authService.getCurrentUser();
        orderDto.setFromDto(order, orderDto);
        order.setStatus(OrderStatus.CREATED);
        order.setUser(user);
        order.setPortfolioID(portfolioID);
        orderRepository.save(order);

        orderValidationService.validateOrder(orderDto, order);
    }

    public void updateOrder(OrderDto newOrderDto, Long orderID) {
        checkAndUpdateOrDeleteOrder(orderID, exchangeConnectivityService, newOrderDto);
    }

    public void deleteOrder(Long orderID) {
        checkAndUpdateOrDeleteOrder(orderID, exchangeConnectivityService, null);
    }

    private void checkAndUpdateOrDeleteOrder(Long orderID, ExchangeConnectivityService exchange, OrderDto newOrderDto) {
        Optional<Order> _order = orderRepository.findById(orderID);

        Order order = _order.orElseThrow(() -> {
            throw new OrderNotFoundException();
        });

        if (order.getStatus().equals(OrderStatus.IN_PROGRESS) || order.getStatus().equals(OrderStatus.PENDING)) {
            if (newOrderDto != null) exchange.updateOrderOnExchange(newOrderDto, order);
            else exchange.cancelOrderOnExchange(order);
        } else {
            throw new OrderNotOpenException();
        }
    }
}
