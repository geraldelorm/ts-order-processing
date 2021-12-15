package io.turntabl.tsops.ClientConnectivity;

import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.repository.PortfolioRepository;
import io.turntabl.tsops.ClientConnectivity.service.PortfolioService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class PortfolioServiceTest {

    @InjectMocks
    PortfolioService portfolioService;

    @Mock
    PortfolioRepository portfolioRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    Portfolio portfolio1 = new Portfolio();
    Portfolio portfolio2 = new Portfolio();
    Portfolio portfolio3 = new Portfolio();
    List<Portfolio> portfolioList = List.of(portfolio1, portfolio2, portfolio3);

    @Test
    void getAllPortfolio() {

        //tell the repository to return the fake list
        when(portfolioRepository.findAll()).thenReturn(portfolioList);
        List<Portfolio> portfolios = portfolioService.getAllPortfolio();
        assertEquals(3, portfolios.size());
    }

    @Test
    void getUserPortfolio() {
    }

    @Test
    void createPortfolio() {
    }
}