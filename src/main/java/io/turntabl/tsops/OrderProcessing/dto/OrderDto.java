package io.turntabl.tsops.OrderProcessing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private String ticker;
    private Integer quantity;
    private Double price;
    private String side ;

}
