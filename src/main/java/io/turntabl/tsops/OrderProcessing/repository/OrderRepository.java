package io.turntabl.tsops.OrderProcessing.repository;


import io.turntabl.tsops.OrderProcessing.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
