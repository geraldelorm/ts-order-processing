package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import io.turntabl.tsops.OrderProcessing.service.TrackOrderOnExchange;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class SendOrderToExchange {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TrackOrderOnExchange trackOrderOnExchange;

    private final OrderRepository orderRepository;

    public void sendOrderToExchangeOne(OrderDto orderDto, Order order){
        String orderUrlFroExOne = "https://exchange.matraining.com/" + "457a1e4f-09ac-4421-9259-fe4d9a999577" + "/order";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject orderJsonObject = new JSONObject();
        orderJsonObject.put("product", orderDto.getTicker());
        orderJsonObject.put("quantity", orderDto.getQuantity());
        orderJsonObject.put("price", orderDto.getPrice());
        orderJsonObject.put("side", orderDto.getSide());

        HttpEntity<String> request = new HttpEntity<String>(orderJsonObject.toString(), headers);

        String orderIDFromExchange = restTemplate.postForObject(orderUrlFroExOne, request, String.class);

        System.out.println(orderIDFromExchange);
        order.setOrderIdFromExchange(orderIDFromExchange);
        order.setStatus("PENDING ON EXCHANGE");
        orderRepository.save(order);

        trackOrderOnExchange.checkOrderStatus(orderIDFromExchange, order);



        //will get the id from the exchange
        //get the order id from the exchange and save it in the db
//        order.setIDFromexchange
        //update status to sent to exchange
        //update the users portfolio with product and quantity
    }
}
