package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.MarketData;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import io.turntabl.tsops.OrderProcessing.service.ExchangeConnectivity;
import io.turntabl.tsops.OrderProcessing.service.MarketDataService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderValidationService {

    private final OrderRepository orderRepository;
    @Autowired
    ExchangeConnectivity exchangeConnectivity;

    public void validateOrder(OrderDto orderDto, Order order) {
        //TODO - VALIDATE ORDER PER THE FOLLOWING

        // do they have enough balance to buy or
        // do they have the asset they wish to sell
        // get marketData for product on both exchanges
        // for sell side - send other to the exchange with highest price
        // for buy side - send other to the exchange with lowest price
        // is the price and amount valid compared to marketData?

        //FOR VALID ORDERS
        order.setStatus("VALIDATED");
        orderRepository.save(order);

        exchangeConnectivity.sendOrderToExchangeOne(orderDto, order);
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
