package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.PortfolioRepository;
import io.turntabl.tsops.ClientConnectivity.repository.ProductRepository;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderInfoFromExchange;
import io.turntabl.tsops.OrderProcessing.entity.OrderStatus;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class ExchangeConnectivityService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    private final String ExchangeKey = "c524d78f-9843-4ba2-90fc-8c7dafdad34f";

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


            checkOrderStatusOnExchange(orderIDFromExchange, order, exchange);
//            jmsTemplate.convertAndSend("orderIDQueue", order.getId().toString());

        } catch (HttpServerErrorException e){
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            log.info("Order Failed on submission to exchange");
        }
    }

    public void updateOrderOnExchange(OrderDto newOrderDto,Order order){
        String orderIDFromExchange = order.getOrderIdFromExchange().replaceAll("^\"+|\"+$", "");
        int exchange = order.getExchangeTradedOn();

        String exchangeUrl;
        if (exchange == 1) {
            exchangeUrl = "https://exchange.matraining.com/";
        } else {
            exchangeUrl = "https://exchange2.matraining.com/";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject orderJsonObject = new JSONObject();
        orderJsonObject.put("product", newOrderDto.getProduct());
        orderJsonObject.put("quantity", newOrderDto.getQuantity());
        orderJsonObject.put("price", newOrderDto.getPrice());
        orderJsonObject.put("side", newOrderDto.getSide());

        HttpEntity<String> request = new HttpEntity<>(orderJsonObject.toString(), headers);

        String orderUpdateUrl = exchangeUrl + ExchangeKey + "/order/" + orderIDFromExchange ;

        ResponseEntity response = restTemplate.exchange(orderUpdateUrl, HttpMethod.PUT, request, String.class);

        log.info(response.getBody().toString());

        if (response.getBody().toString().equals("true")){
            log.info("Order Updated " );
            order.setStatus(OrderStatus.UPDATED);
            order.setPrice(newOrderDto.getPrice());
            order.setQuantity(newOrderDto.getQuantity());
            order.setSide(newOrderDto.getSide());
            order.setProduct(newOrderDto.getProduct());
            orderRepository.save(order);
        } else {
            log.info("Order Not Updated" );
        }
    }

    public void cancelOrderOnExchange(Order order){
        String orderIDFromExchange = order.getOrderIdFromExchange().replaceAll("^\"+|\"+$", "");
        int exchange = order.getExchangeTradedOn();

        String exchangeUrl;
        if (exchange == 1) {
            exchangeUrl = "https://exchange.matraining.com/";
        } else {
            exchangeUrl = "https://exchange2.matraining.com/";
        }

        log.info("Order Status: " + order.getStatus());
        String orderDeletionUrl = exchangeUrl + ExchangeKey + "/order/" + orderIDFromExchange ;

        ResponseEntity response = restTemplate.exchange(orderDeletionUrl, HttpMethod.DELETE, null, String.class);

        log.info(response.getBody().toString());

        if (response.getBody().toString().equals("true")){
            log.info("Order Canceled " );
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
        } else {
            log.info("Order Not Canceled " );
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

                //updating account balance
                User user = order.getUser();
                if (order.getSide().equals("SELL")){
                    user.setAccount_balance(user.getAccount_balance() + (order.getPrice() * order.getQuantity()));
                } else {
                    user.setAccount_balance(user.getAccount_balance() - (order.getPrice() * order.getQuantity()));
                }
                userRepository.save(user);
                log.info("Balance updated");

                // updating product quantity for user
                Portfolio portfolio = portfolioRepository.getById(order.getPortfolioID());
                Product product = portfolio.getProducts().stream().filter(p -> p.getTicker().equals(order.getProduct())).findFirst().get();

                if (order.getSide().equals("BUY")){
                    product.setQuantity(product.getQuantity() + (order.getQuantity()));
                } else {
                    product.setQuantity(product.getQuantity() - (order.getQuantity()));
                }
                productRepository.save(product);
                log.info("Product updated");

            }
        }
    }
}
