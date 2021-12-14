package io.turntabl.tsops.OrderProcessing.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.turntabl.tsops.OrderProcessing.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String product;
    private Integer quantity;
    private Double price;
    private String side;

    @JsonIgnore
    public void setFromDto(Order order, OrderDto orderDto) {
        order.setProduct(orderDto.getProduct());
        order.setQuantity(orderDto.getQuantity());
        order.setPrice(orderDto.getPrice());
        order.setSide(orderDto.getSide());
    }
}
