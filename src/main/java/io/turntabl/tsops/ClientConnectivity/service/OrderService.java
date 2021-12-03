package io.turntabl.tsops.ClientConnectivity.service;

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
        //validate the order
        //save successful orders with the id from the exchange


    }

    //validate an order
    public void validateOrder(OrderDto orderDto, Long userId){
        // check if user exists
        // do they have enough balance to buy or
        // do they have the asset they wish to sell
        // is the price and amount valid compared to marketData?
        //get the order id from the exchange and save it in the db
    }
//    @Autowired
//    MarketDataService marketDataService;
//
//    @PostConstruct
//    public void printMarketData(){
//        marketDataService.getMarketDataList();
//    }
}
