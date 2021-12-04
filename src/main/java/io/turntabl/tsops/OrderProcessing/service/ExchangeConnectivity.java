package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderInfo;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ExchangeConnectivity {

    private final OrderRepository orderRepository;

    @Autowired
    RestTemplate restTemplate;

    @Value("${privateKey}")
    private final String ExchangeKey = "457a1e4f-09ac-4421-9259-fe4d9a999577";

    public void sendOrderToExchangeOne(OrderDto orderDto, Order order) {
        String orderUrlFroExOne = "https://exchange.matraining.com/" + ExchangeKey + "/order";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject orderJsonObject = new JSONObject();
        orderJsonObject.put("product", orderDto.getProduct());
        orderJsonObject.put("quantity", orderDto.getQuantity());
        orderJsonObject.put("price", orderDto.getPrice());
        orderJsonObject.put("side", orderDto.getSide());

        HttpEntity<String> request = new HttpEntity<String>(orderJsonObject.toString(), headers);

        String orderIDFromExchange = restTemplate.postForObject(orderUrlFroExOne, request, String.class);

        System.out.println(orderIDFromExchange);
        order.setOrderIdFromExchange(orderIDFromExchange);
        order.setStatus("PENDING ON EXCHANGE");
        orderRepository.save(order);

        checkOrderStatusOnExchangeOne(orderIDFromExchange, order);

        //will get the id from the exchange
        //get the order id from the exchange and save it in the db
//        order.setIDFromexchange
        //update status to sent to exchange
        //update the users portfolio with product and quantity
    }
    public void checkOrderStatusOnExchangeOne(String orderIdFromEx, Order order){
        String orderID = orderIdFromEx. replaceAll("^\"+|\"+$", "");
        System.out.println(orderID);

        while (order.getStatus() != "EXECUTED"){
            System.out.println(order.getStatus());
            String orderStatusUrlFroExOne = "https://exchange.matraining.com/" + ExchangeKey + "/order/" + orderID ;
            System.out.println(orderStatusUrlFroExOne);

            OrderInfo orderInfoFromExchange = restTemplate.getForObject(orderStatusUrlFroExOne, OrderInfo.class);

            System.out.println(orderInfoFromExchange);

            int cumulativeQuantity = orderInfoFromExchange.getCumulativeQuantity();
            int quantity = orderInfoFromExchange.getQuantity();
            System.out.println(cumulativeQuantity + " , " + quantity);
            if (cumulativeQuantity == quantity){
                System.out.println("EXECUTED");
                order.setStatus("EXECUTED");
                orderRepository.save(order);
            }
        }
    }

//    public void sendOrderToExchangeTwo(OrderDto orderDto, Order order) {
//        String orderUrlFroExOne = "https://exchange2.matraining.com/" +"457a1e4f-09ac-4421-9259-fe4d9a999577" + "/order";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        JSONObject orderJsonObject = new JSONObject();
//        orderJsonObject.put("product", orderDto.getTicker());
//        orderJsonObject.put("quantity", orderDto.getQuantity());
//        orderJsonObject.put("price", orderDto.getPrice());
//        orderJsonObject.put("side", orderDto.getSide());
//
//        HttpEntity<String> request = new HttpEntity<String>(orderJsonObject.toString(), headers);
//
//        String orderIDFromExchange = restTemplate.postForObject(orderUrlFroExOne, request, String.class);
//
//        System.out.println(orderIDFromExchange);
//        order.setOrderIdFromExchange(orderIDFromExchange);
//        order.setStatus("PENDING ON EXCHANGE");
//        orderRepository.save(order);

//        checkOrderStatusOnExchangeTwo(orderIDFromExchange, order);

        //will get the id from the exchange
        //get the order id from the exchange and save it in the db
//        order.setIDFromexchange
        //update status to sent to exchange
        //update the users portfolio with product and quantity
//    }


//    public void checkOrderStatusOnExchangeTwo(String orderIdFromEx, Order order){
//        String orderUrlFroExOne = "https://exchange2.matraining.com/" + ExchangeKey + "/order/" + orderIdFromEx ;
//
//        Object orderFromExchange = restTemplate.getForObject(orderUrlFroExOne, String.class);
//
//        System.out.println(orderFromExchange);
//    }

}
