package io.turntabl.tsops.ClientConnectivity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @JsonProperty("BUY_LIMIT")
    private int buyLimit;

    @JsonProperty("SELL_LIMIT")
    private int sellLimit;

    @JsonProperty("TICKER")
    private String ticker;

    @JsonProperty("MAX_PRICE_SHIFT")
    private float maxPriceShift;

    @JsonProperty("BID_PRICE")
    private float bidPrice;

    @JsonProperty("ASK_PRICE")
    private float askPrice;

    @JsonProperty("LAST_TRADED_PRICE")
    private float lastTradedPrice;

}