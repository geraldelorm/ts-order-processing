package io.turntabl.tsops.OrderProcessing.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo {

    @JsonProperty("product")
    private String product;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("side")
    private String side;

    @JsonProperty("executions")
    private List executions;

    @JsonProperty("cumulativeQuantity")
    private int cumulativeQuantity;

}
