package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.MarketData;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import io.turntabl.tsops.OrderProcessing.service.ExchangeConnectivity;
import io.turntabl.tsops.OrderProcessing.service.MarketDataService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class OrderValidationService {

    private final OrderRepository orderRepository;

    @Autowired
    ExchangeConnectivity exchangeConnectivity;

    public void validateOrder(OrderDto orderDto, Order order) {
        MarketData mdForProductOnExOne = marketDataForAProductOnExOne(orderDto.getProduct());
        MarketData mdForProductOnExTwo = marketDataForAProductOnExTwo(orderDto.getProduct());

        //TODO

        // do they have enough balance to buy or
        // do they have the asset they wish to sell

        if(mdForProductOnExOne == null){
            System.out.println("got to the end");
            order.setStatus("INVALID");
            log.info("Oder: " + orderDto + " Product is not available on the exchange");
            orderRepository.save(order);
        }
        else if (orderDto.getSide().equals("BUY") && orderDto.getQuantity() > mdForProductOnExOne.getBuyLimit()) {
            order.setStatus("INVALID");
            log.info("Oder: " + orderDto + " quantity is above buy limit");
            orderRepository.save(order);
        } else if (orderDto.getSide().equals("SELL")  && orderDto.getQuantity() > mdForProductOnExOne.getSellLimit()) {
            order.setStatus("INVALID");
            log.info("Oder: " + orderDto + " quantity is above sell limit");
            orderRepository.save(order);
        } else {
            order.setStatus("VALIDATED");
            orderRepository.save(order);
            log.info("Order is Valid and was sent to exchange");

            //TODO
            //DECIDE WHICH EXCHANGE TO SEND TO
            // for sell side - send other to the exchange with highest price
            // for buy side - send other to the exchange with lowest price
            exchangeConnectivity.sendOrderToExchange(orderDto, order, 1);
        }
    }

    public MarketData marketDataForAProductOnExOne(String productTicker) {
        return MarketDataService.listOfMarketDataFromExchangeOne.stream()
                .filter(marketData -> marketData.getTicker().equals(productTicker))
                .findAny().get();
    }

    public MarketData marketDataForAProductOnExTwo(String productTicker) {
        return MarketDataService.listOfMarketDataFromExchangeTwo.stream()
                .filter(marketData -> marketData.getTicker().equals(productTicker))
                .findFirst().get();
    }
}
