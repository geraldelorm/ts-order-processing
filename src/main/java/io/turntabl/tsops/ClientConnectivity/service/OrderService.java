package io.turntabl.tsops.ClientConnectivity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.turntabl.tsops.ClientConnectivity.dto.MarketData;
import io.turntabl.tsops.ClientConnectivity.dto.OrderDto;
import io.turntabl.tsops.ClientConnectivity.entity.Order;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.OrderRepository;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    @Autowired
    MarketDataService marketDataService;

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    //get all orders
    public List<Order> getAllOrder(){
        return orderRepository.findAll();
    }

    //get one user's order
    public List<Order> getUserOrder(Long userId){
        User user = userRepository.getById(userId);
        return user.getOrders();
    }

    //create an order
    public void createOrder(OrderDto orderDto, Long userId){
        Order order = new Order();
        User user = userRepository.getById(userId);
        order.setOrderIdFromExchange(orderDto.getOrderIdFromExchange());
        order.setUser(user);
        orderRepository.save(order);
        //send order to reporting service and validate order
//        validateOrder(orderDto);
    }

    //validate an order
    public void validateOrder(OrderDto orderDto){

        // do they have enough balance to buy or
        // do they have the asset they wish to sell

        // get marketData fro product on both exchanges
        // for sell side - send other to the exchange with highest price
        // for buy side - send other to the exchange with lowest price
        // is the price and amount valid compared to marketData?
        sendOrderToExchange(orderDto);
    }

    public void sendOrderToExchange(OrderDto orderDto){

    }

    public void trackOrderStatus(String orderId){

    }


    public MarketData marketDataForAProductOnExOne(String productTicker) {
        return MarketDataService.listOfMarketDataFromExchangeOne.stream()
                .filter(marketData -> marketData.getTicker().equals(productTicker))
                .findFirst().get();
    }
    public MarketData marketDataForAProductOnExTwo(String productTicker) {
        return MarketDataService.listOfMarketDataFromExchangeTwo.stream()
                .filter(marketData -> marketData.getTicker().equals(productTicker))
                .findFirst().get();
    }

}
