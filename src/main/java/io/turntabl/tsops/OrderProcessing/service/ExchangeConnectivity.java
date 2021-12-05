package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderInfoFromExchange;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
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

        HttpEntity<String> request = new HttpEntity<>(orderJsonObject.toString(), headers);
        String orderIDFromExchange = restTemplate.postForObject(orderUrlFroExOne, request, String.class);

        order.setOrderIdFromExchange(orderIDFromExchange);
        order.setStatus("SENT TO EXCHANGE");
        orderRepository.save(order);

        //TODO //Send order details to reporting service for tracking
        checkOrderStatusOnExchangeOne(orderIDFromExchange, order);
    }

    public void checkOrderStatusOnExchangeOne(String orderIdFromEx, Order order){
        String orderID = orderIdFromEx.replaceAll("^\"+|\"+$", "");

        while (!order.getStatus().equals("EXECUTED")){
            log.info("Order Status: " + order.getStatus());
            String orderStatusUrlFroExOne = "https://exchange.matraining.com/" + ExchangeKey + "/order/" + orderID ;

            OrderInfoFromExchange orderInfoFromExchange = restTemplate.getForObject(orderStatusUrlFroExOne, OrderInfoFromExchange.class);
            log.info("Order Info From Exchange: " + orderInfoFromExchange);

            int cumulativeQuantity = orderInfoFromExchange.getCumulativeQuantity();
            int quantity = orderInfoFromExchange.getQuantity();

            if (cumulativeQuantity == quantity){
                order.setStatus("FULLY EXECUTED");
                orderRepository.save(order);
            } else if (cumulativeQuantity > 0){
                order.setStatus("PARTLY EXECUTED");
                orderRepository.save(order);
            }
        }
    }
}
