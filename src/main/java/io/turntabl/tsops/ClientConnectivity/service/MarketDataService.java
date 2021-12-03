package io.turntabl.tsops.ClientConnectivity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.tsops.ClientConnectivity.dto.MarketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.fasterxml.jackson.databind.cfg.CoercionInputShape.Array;

@Service
public class MarketDataService {

    Logger logger =  LoggerFactory.getLogger(MarketDataService.class);

    public static List<MarketData> listOfMarketDataFromExchangeOne = new ArrayList<>();
    public static List<MarketData> listOfMarketDataFromExchangeTwo = new ArrayList<>();

    ObjectMapper objectMapper = new ObjectMapper();

    public void marketDataFromExOne(String message) throws JsonProcessingException {
        MarketData[] md =  objectMapper.readValue(message, MarketData[].class);
        listOfMarketDataFromExchangeOne = Arrays.asList(md);

        logger.info("Consumed Message From EX 1 {}", listOfMarketDataFromExchangeOne);
    }

    public void marketDataFromExTwo(String message) throws JsonProcessingException {
        MarketData[] md =  objectMapper.readValue(message, MarketData[].class);
        listOfMarketDataFromExchangeTwo = Arrays.asList(md);

        logger.info("Consumed Message From EX 2 {}", listOfMarketDataFromExchangeTwo);
    }
}
