package io.turntabl.tsops.ClientConnectivity.controller;

import io.turntabl.tsops.ClientConnectivity.dto.PortfolioDto;
import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.security.jwtUtils.JwtAuthenticationFilter;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.ClientConnectivity.service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolio")
@AllArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;


    @GetMapping
    public List<Portfolio> geAllPortfolio(){
        return portfolioService.getAllPortfolio();
    }


    @GetMapping(path = "/user")
    public List<Portfolio> getUserPortfolio(){
         return portfolioService.getUserPortfolio();
    }

    @PostMapping(path = "/create")
    public ResponseEntity<Void> createPortfolio(@RequestBody PortfolioDto portfolioDto){
         portfolioService.createPortfolio(portfolioDto);
         return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
