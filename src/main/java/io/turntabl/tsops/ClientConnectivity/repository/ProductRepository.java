package io.turntabl.tsops.ClientConnectivity.repository;

import io.turntabl.tsops.ClientConnectivity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}