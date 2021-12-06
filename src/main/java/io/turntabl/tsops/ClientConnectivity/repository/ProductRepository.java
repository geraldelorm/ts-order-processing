package io.turntabl.tsops.ClientConnectivity.repository;

import io.turntabl.tsops.ClientConnectivity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

}