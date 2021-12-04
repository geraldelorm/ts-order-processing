package io.turntabl.tsops.OrderProcessing.service;

import io.turntabl.tsops.OrderProcessing.entity.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TrackOrderOnExchange {


    public void checkOrderStatus(String orderIdFromEx, Order order){
        // tracker oder with ID from exchange until its complete
    }
}
