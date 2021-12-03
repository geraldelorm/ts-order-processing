package io.turntabl.tsops.ClientConnectivity.dto;

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
