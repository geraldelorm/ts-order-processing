package io.turntabl.tsops.ClientConnectivity.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.tsops.ClientConnectivity.dto.MarketData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarketDataService {

    Logger logger =  LoggerFactory.getLogger(MarketDataService.class);

    private ArrayList listOfMarketDataFromExchangeOne;
    private ArrayList listOfMarketDataFromExchangeTwo;

    ObjectMapper objectMapper = new ObjectMapper();

    public void marketDataFromExOne(String message) throws JsonProcessingException {
        listOfMarketDataFromExchangeOne = objectMapper.readValue(message, ArrayList.class);
        System.out.println(listOfMarketDataFromExchangeOne);

//        logger.info("Consumed Message From EX 1 {}", listOfMarketDataFromExchangeOne);
    }

    public void marketDataFromExTwo(String message) throws JsonProcessingException {
        listOfMarketDataFromExchangeTwo = objectMapper.readValue(message, ArrayList.class);
        System.out.println(listOfMarketDataFromExchangeTwo);

//        logger.info("Consumed Message From EX 1 {}", listOfMarketDataFromExchangeTwo);
    }

    public ArrayList getListOfMarketDataFromExchangeOne(){
        return listOfMarketDataFromExchangeOne;
    }

    public ArrayList getListOfMarketDataFromExchangeTwo(){
        return listOfMarketDataFromExchangeTwo;
    }
}
