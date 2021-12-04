package io.turntabl.tsops.ClientConnectivity.controller;

import io.turntabl.tsops.ClientConnectivity.dto.PortfolioDto;
import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/portfolio")
@AllArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public List<Portfolio> geAllPortfolio(){
        return portfolioService.getAllPortfolio();
    }

    @GetMapping(path = "/{userId}")
    public List<Portfolio> getUserPortfolio(@PathVariable("userId")Long userId){
        return portfolioService.getUserPortfolio(userId);
    }

    @PostMapping(path = "/{userId}")
    public ResponseEntity<Void> createPortfolio(@RequestBody PortfolioDto portfolioDto, @PathVariable("userId")Long userId){
         portfolioService.createPortfolio(portfolioDto, userId);
         return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
