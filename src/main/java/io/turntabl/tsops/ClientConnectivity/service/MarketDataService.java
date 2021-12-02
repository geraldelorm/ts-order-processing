package io.turntabl.tsops.ClientConnectivity.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.turntabl.tsops.ClientConnectivity.dto.Product;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MarketDataService implements MessageListener {

    Logger logger =  LoggerFactory.getLogger(MarketDataService.class);

    List<Product> marketData;

    @SneakyThrows
    @Override
    public void onMessage(Message message,  byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        String body = new String(message.getBody());

        marketData = objectMapper.readValue(body, List.class);
//        logger.info("Consumed Message {}", Arrays.asList(marketData));

    }

    public List<Product> getMarketData() {
        return marketData;
    }
}

