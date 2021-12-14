package io.turntabl.tsops.OrderProcessing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.io.Serializable;
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

    @Column(name = "product")
    private String product;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Double price;

    @Column(name = "side")
    private String side ;

    @Column(name = "exchangeTradedOn")
    private Integer exchangeTradedOn;

    @Column(name = "order_id_exchange")
    private String orderIdFromExchange;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @JoinColumn(name = "portfolioID", nullable = false)
    private Long portfolioID;
}