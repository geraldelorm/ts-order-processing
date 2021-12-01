package io.turntabl.tsops.ClientConnectivity.repository;

import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {


}
