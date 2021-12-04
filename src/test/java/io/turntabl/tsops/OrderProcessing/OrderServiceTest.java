package io.turntabl.tsops.OrderProcessing;

import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import io.turntabl.tsops.OrderProcessing.service.OrderService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    OrderRepository orderRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

    }

    Order order1 = new Order();
    Order order2 = new Order();
    Order order3 = new Order();
    List<Order> orderList = List.of(order1, order2, order3);

    @Test
    void getAllOrder() {
        when(orderRepository.findAll()).thenReturn(orderList);

        List<Order> orders = orderService.getAllOrder();

        assertEquals(3, orders.size());
    }

    @Test
    void getUserOrder() {
    }

    @Test
    void createOrder() {
    }
}