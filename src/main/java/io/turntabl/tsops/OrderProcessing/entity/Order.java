package io.turntabl.tsops.OrderProcessing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;


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

    @Column(name = "product")
    private String product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "side")
    private String side ;

    @Column(name = "order_id_exchange")
    private String orderIdFromExchange;

    @Column(name = "status")
    private String status;

    @Column(name ="created_at")
    @Temporal(TemporalType.TIMESTAMP) // set automatically
    private java.util.Date created_At;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "client_id", nullable = false)
    private User user;



}