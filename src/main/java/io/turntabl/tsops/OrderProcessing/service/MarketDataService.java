package io.turntabl.tsops.OrderProcessing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.tsops.OrderProcessing.entity.MarketData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MarketDataService {

    public static List<MarketData> listOfMarketDataFromExchangeOne = new ArrayList<>();
    public static List<MarketData> listOfMarketDataFromExchangeTwo = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    public void marketDataFromExOne(String message) throws JsonProcessingException {
        MarketData[] md = objectMapper.readValue(message, MarketData[].class);
        listOfMarketDataFromExchangeOne = Arrays.asList(md);

        log.info("Consumed Message From Exchange 1 {}", listOfMarketDataFromExchangeOne);
    }

    public void marketDataFromExTwo(String message) throws JsonProcessingException {
        MarketData[] md = objectMapper.readValue(message, MarketData[].class);
        listOfMarketDataFromExchangeTwo = Arrays.asList(md);

        log.info("Consumed Message From Exchange 2 {}", listOfMarketDataFromExchangeTwo);
    }
}
