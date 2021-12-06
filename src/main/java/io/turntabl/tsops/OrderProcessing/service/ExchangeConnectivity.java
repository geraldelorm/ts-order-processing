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
import org.springframework.web.client.HttpServerErrorException;
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

    public void sendOrderToExchange(OrderDto orderDto, Order order, int exchange) {
        String exchangeUrl;
        if (exchange == 1) {
            exchangeUrl = "https://exchange.matraining.com/";
        } else {
            exchangeUrl = "https://exchange2.matraining.com/";
        }
        String orderSubmissionUrl = exchangeUrl + ExchangeKey + "/order";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject orderJsonObject = new JSONObject();
        orderJsonObject.put("product", orderDto.getProduct());
        orderJsonObject.put("quantity", orderDto.getQuantity());
        orderJsonObject.put("price", orderDto.getPrice());
        orderJsonObject.put("side", orderDto.getSide());

        HttpEntity<String> request = new HttpEntity<>(orderJsonObject.toString(), headers);

        try {
            String orderIDFromExchange = restTemplate.postForObject(orderSubmissionUrl, request, String.class);
            order.setOrderIdFromExchange(orderIDFromExchange);
            order.setStatus("SENT");
            orderRepository.save(order);

            //TODO //Send order details to reporting service for tracking
            checkOrderStatusOnExchange(orderIDFromExchange, order, exchange);

        } catch (HttpServerErrorException e){
            order.setStatus("FAILED");
            orderRepository.save(order);
            log.info("Order Failed on submission to exchange");
        }
    }

    public void checkOrderStatusOnExchange(String orderIdFromEx, Order order, int exchange){
        String orderID = orderIdFromEx.replaceAll("^\"+|\"+$", "");
        String exchangeUrl;
        if (exchange == 1) {
            exchangeUrl = "https://exchange.matraining.com/";
        } else {
            exchangeUrl = "https://exchange2.matraining.com/";
        }

        while (!order.getStatus().equals("EXECUTED")){
            log.info("Order Status: " + order.getStatus());
            String orderStatusUrl = exchangeUrl + ExchangeKey + "/order/" + orderID ;

            try{
                OrderInfoFromExchange orderInfoFromExchange = restTemplate.getForObject(orderStatusUrl, OrderInfoFromExchange.class);
                log.info("Order Info From Exchange: " + orderInfoFromExchange);

                int cumulativeQuantity = orderInfoFromExchange.getCumulativeQuantity();

                if (cumulativeQuantity > 0){
                    order.setStatus("PARTLY EXECUTED");
                    orderRepository.save(order);
                }
            } catch (HttpServerErrorException e) {
                order.setStatus("EXECUTED");
                orderRepository.save(order);
                log.info("Order Status: EXECUTED" );
            }
        }
    }
}
