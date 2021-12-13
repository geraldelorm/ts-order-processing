package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.PortfolioRepository;
import io.turntabl.tsops.ClientConnectivity.repository.ProductRepository;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.OrderProcessing.dto.OrderDto;
import io.turntabl.tsops.OrderProcessing.entity.MarketData;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import io.turntabl.tsops.OrderProcessing.entity.OrderInfoFromExchange;
import io.turntabl.tsops.OrderProcessing.entity.OrderStatus;
import io.turntabl.tsops.OrderProcessing.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderValidationService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;
    private final PortfolioRepository portfolioRepository;

    @Autowired
    ExchangeConnectivityService exchangeConnectivityService;

    @Autowired
    RestTemplate restTemplate;

    public void validateOrder(OrderDto orderDto, Order order) {
        User user = authService.getCurrentUser();
        Product product;

        MarketData mdForProductOnExOne = marketDataForAProductOnExOne(orderDto.getProduct());
        MarketData mdForProductOnExTwo = marketDataForAProductOnExTwo(orderDto.getProduct());

        List<OrderInfoFromExchange> orderBookForProductOnExOne = orderBookForAProductOnExOne(orderDto.getProduct());
        List<OrderInfoFromExchange> orderBookForProductOnExTwo = orderBookForAProductOnExTwo(orderDto.getProduct());

        MarketData marketDataToValidateWith;
        int exchangeToTradeOn;

        Portfolio portfolio = portfolioRepository.getById(order.getPortfolioID());

        if (portfolio.getProducts().stream().anyMatch(p -> p.getTicker().equals(orderDto.getProduct()))){
            product = portfolio.getProducts().stream().filter(p -> p.getTicker().equals(orderDto.getProduct())).findFirst().get();
        } else {
            product = new Product();
            product.setTicker(orderDto.getProduct());
            product.setQuantity(0);
            product.setUser(user);
            product.setPortfolioId(order.getPortfolioID());
            productRepository.save(product);

            portfolio.addProduct(product);
            portfolioRepository.save(portfolio);

        }


        if(orderDto.getSide().equals("SELL")){
            if (mdForProductOnExOne.getLastTradedPrice() > mdForProductOnExTwo.getLastTradedPrice()){
                marketDataToValidateWith = mdForProductOnExOne;
                exchangeToTradeOn = 1;
            } else {
                marketDataToValidateWith = mdForProductOnExTwo;
                exchangeToTradeOn = 2;
            }
            if (userHasTheEnoughProduct(product, orderDto)) {
                if (priceIsReasonable(orderDto, marketDataToValidateWith)){
                    if (quantityIsReasonable(orderDto, marketDataToValidateWith)){
                        exchangeConnectivityService.sendOrderToExchange(orderDto, order, exchangeToTradeOn);
                    }
                }
            } else {
                order.setStatus(OrderStatus.INVALID);
                orderRepository.save(order);
            }
        }else if (orderDto.getSide().equals("BUY")){
            if (mdForProductOnExOne.getLastTradedPrice() < mdForProductOnExTwo.getLastTradedPrice()){
                marketDataToValidateWith = mdForProductOnExOne;
                exchangeToTradeOn = 1;
            } else {
                marketDataToValidateWith = mdForProductOnExTwo;
                exchangeToTradeOn = 2;
            }
            if (userHasEnoughFunds(user, orderDto)){
                if (priceIsReasonable(orderDto, marketDataToValidateWith)) {
                    if (quantityIsReasonable(orderDto, marketDataToValidateWith)){
                        exchangeConnectivityService.sendOrderToExchange(orderDto, order, exchangeToTradeOn);
                    }
                }
            }
            else {
                order.setStatus(OrderStatus.INVALID);
                orderRepository.save(order);
            }
        }
            //TODO
            // DECIDE WHICH EXCHANGE TO SEND TO BASED ON ORDER BOOK
    }

    //Validation Methods
    private boolean userHasEnoughFunds(User user, OrderDto orderDto){
        if((orderDto.getPrice() * orderDto.getQuantity()) > user.getAccount_balance()){
            log.info("User Doesn't have enough funds to make this purchase");
            return false;
        }
        return true;
    }

    private boolean userHasTheEnoughProduct(Product product, OrderDto orderDto){
        if (orderDto.getSide().equals("SELL")){
            if (orderDto.getQuantity() > product.getQuantity()){
                log.info("User Doesn't have enough of the quantity to sell");
                return false;
            }
        }
        return true;
    }

    private boolean quantityIsReasonable(OrderDto orderDto, MarketData marketData){
        if (orderDto.getSide().equals("SELL")){
            if(orderDto.getQuantity() > marketData.getSellLimit() || orderDto.getQuantity() <= 0){
                log.info("Quantity is unreasonable");
                return false;
            }
        } else if(orderDto.getSide().equals("BUY")){
            if(orderDto.getQuantity() > marketData.getBuyLimit() || orderDto.getQuantity() <= 0){
                log.info("Quantity is unreasonable");
                return false;
            }
        }
        return true;
    }

    private  boolean priceIsReasonable(OrderDto orderDto, MarketData marketData){
        Float upperLimit = marketData.getLastTradedPrice() + marketData.getMaxPriceShift();
        Float lowerLimit = marketData.getLastTradedPrice() - marketData.getMaxPriceShift();

        if (orderDto.getPrice() > upperLimit || orderDto.getPrice() < lowerLimit || orderDto.getPrice() < 0){
            log.info("Price is unreasonable");
            return  false;
        }
        return true;

    }

    //MarketData and OrderBook
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

    public List<OrderInfoFromExchange> orderBookForAProductOnExOne(String productTicker){
        String url = "https://exchange.matraining.com/orderbook/" + productTicker;
        return restTemplate.getForObject(url, List.class);
    }

    public List<OrderInfoFromExchange> orderBookForAProductOnExTwo(String productTicker){
        String url = "https://exchange2.matraining.com/orderbook/" + productTicker;
        return restTemplate.getForObject(url, List.class);
    }
}
