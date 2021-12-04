package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderCreationService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    @Autowired
    OrderValidationService orderValidationService;

    public void createOrder(OrderDto orderDto, Long userId) {
        //get the order
        //save the order with user id
        //validate the order
        //send order to reporting service
        //and send order to exchange

        Order order = new Order();
        order.setTicker(orderDto.getTicker());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
//        order.setCreated_At();
        order.setStatus("CREATED");
        order.setUser(userRepository.getById(userId));
        orderRepository.save(order);
        System.out.println(orderDto);

        orderValidationService.validateOrder(orderDto, order);
    }
}
