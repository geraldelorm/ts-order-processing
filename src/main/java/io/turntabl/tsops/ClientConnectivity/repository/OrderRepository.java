package io.turntabl.tsops.ClientConnectivity.repository;


import io.turntabl.tsops.ClientConnectivity.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
