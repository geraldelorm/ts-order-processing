package io.turntabl.tsops.ClientConnectivity.controller;

import io.turntabl.tsops.ClientConnectivity.dto.PortfolioDto;
import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.exception.ResponseHandler;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
import io.turntabl.tsops.ClientConnectivity.service.PortfolioService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/portfolio")
@AllArgsConstructor
public class PortfolioController {
    private final String CLIENT = "ROLE_CLIENT";
    private final String ADMIN = "ROLE_ADMIN";

    @Autowired
    private final PortfolioService portfolioService;

    @Autowired
    private final AuthService authService;

    @Secured(ADMIN)
    @GetMapping
    public ResponseEntity<Object> geAllPortfolio() {
        return ResponseHandler
                .builder()
                .data(portfolioService.getAllPortfolio())
                .status(HttpStatus.OK)
                .build();
    }

    @Secured(CLIENT)
    @GetMapping(path = "/user")
    public ResponseEntity<Object> getUserPortfolio() {
        return ResponseHandler
                .builder()
                .data(portfolioService.getUserPortfolio())
                .status(HttpStatus.OK)
                .build();
    }

    @Secured(CLIENT)
    @PostMapping(path = "/create")
    public ResponseEntity<Object> createPortfolio(@RequestBody PortfolioDto portfolioDto) {
            portfolioService.createPortfolio(portfolioDto);
            return ResponseHandler
                    .builder()
                    .message("Portfolio is created")
                    .status(HttpStatus.CREATED)
                    .build();
    }

}
