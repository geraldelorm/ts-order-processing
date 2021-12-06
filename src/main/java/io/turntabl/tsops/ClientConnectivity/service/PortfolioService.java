package io.turntabl.tsops.ClientConnectivity.service;


import io.turntabl.tsops.ClientConnectivity.dto.PortfolioDto;
import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.PortfolioRepository;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    //get all portfolios
    public List<Portfolio> getAllPortfolio(){
        return portfolioRepository.findAll();
    }

    //get one user's portfolio
    public List<Portfolio> getUserPortfolio(){
        User user = authService.getCurrentUser();
        if(user.getUserRole().equals("client")){
            return user.getPortfolio();
        }
        else {
            List<Portfolio> portfolios = new ArrayList<>();
            return portfolios;
        }

    }

    //create a portfolio
    public void createPortfolio(PortfolioDto portfolioDto){
        Portfolio portfolio = new Portfolio();
        User user = authService.getCurrentUser();
        portfolio.setName(portfolioDto.getName());
        portfolio.setDescription(portfolioDto.getDescription());
        portfolio.setUser(user);
        portfolioRepository.save(portfolio);
    }
}
