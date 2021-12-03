package io.turntabl.tsops.ClientConnectivity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "side")
    private String side ;

    @Column(name = "order_id_exchange")
    private String orderIdFromExchange;

    @Column(name ="created_at")
    private Date created_At;


    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id", nullable = false)
    private User user;



}