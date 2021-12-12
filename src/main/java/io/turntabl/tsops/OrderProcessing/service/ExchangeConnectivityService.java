package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderInfoFromExchange;
import io.turntabl.tsops.OrderProcessing.entity.OrderStatus;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class ExchangeConnectivityService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JmsTemplate jmsTemplate;

    private final OrderRepository orderRepository;

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
            order.setStatus(OrderStatus.PENDING);
            order.setExchangeTradedOn(exchange);
            orderRepository.save(order);

            //TODO
            // Send order details to reporting service for tracking
            //checkOrderStatusOnExchange(orderIDFromExchange, order, exchange);
            jmsTemplate.convertAndSend("orderIDQueue", orderIDFromExchange);

        } catch (HttpServerErrorException e){
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            log.info("Order Failed on submission to exchange");
        }
    }

    public void updateOrderOnExchange(OrderDto newOrderDto,String orderIDFromExchange, int exchange){

    }

    public void deleteOrderOnExchange(OrderDto newOrderDto,String orderIDFromExchange, int exchange){

    }
  
    public void checkOrderStatusOnExchange(String orderIdFromEx, Order order, int exchange){
        String orderID = orderIdFromEx.replaceAll("^\"+|\"+$", "");
        String exchangeUrl;
        if (exchange == 1) {
            exchangeUrl = "https://exchange.matraining.com/";
        } else {
            exchangeUrl = "https://exchange2.matraining.com/";
        }

        while (!order.getStatus().equals(OrderStatus.EXECUTED)){
            log.info("Order Status: " + order.getStatus());
            String orderStatusUrl = exchangeUrl + ExchangeKey + "/order/" + orderID ;

            try{
                OrderInfoFromExchange orderInfoFromExchange = restTemplate.getForObject(orderStatusUrl, OrderInfoFromExchange.class);
                log.info("Order Info From Exchange: " + orderInfoFromExchange);

                int cumulativeQuantity = orderInfoFromExchange.getCumulativeQuantity();

                if (cumulativeQuantity > 0){
                    order.setStatus(OrderStatus.IN_PROGRESS);
                    orderRepository.save(order);
                }
            } catch (HttpServerErrorException e) {
                order.setStatus(OrderStatus.EXECUTED);
                orderRepository.save(order);
                log.info("Order Status: EXECUTED" );

                //TODO
                // IF order is on the sell side increase account Balance
                // IF order is on the buy side decrease account Balance
                // Increase or Decrease product quantity for the user
            }
        }
    }
}
