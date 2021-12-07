package io.turntabl.tsops.ClientConnectivity.service;

import io.turntabl.tsops.ClientConnectivity.dto.ProductDto;
import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.PortfolioRepository;
import io.turntabl.tsops.ClientConnectivity.repository.ProductRepository;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final PortfolioRepository portfolioRepository;
    private final AuthService authService;

    //get all product
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    //get all user's products
    public List<Product> getUserProduct(){
        User user = authService.getCurrentUser();
        return user.getProducts();
    }

    //get products from a specific portfolio
    public List<Product> getProductsFromPortfolio(Long portfolioId){
        Portfolio portfolio = portfolioRepository.getById(portfolioId);
        return portfolio.getProducts();
    }

    //create a product
    public void createProduct(ProductDto productDto, Long portfolioId){
        //TODO :  when you successfully buy a product implement logic to be assigned to a user
        //TODO : get this from the ordr the person has made
        Product product = new Product();
        User user = authService.getCurrentUser();
        Portfolio portfolio = portfolioRepository.getById(portfolioId);
        product.setTicker(productDto.getTicker());
        product.setQuantity(productDto.getQuantity());
        product.setPortfolioId(portfolioId);
        portfolio.addProduct(product);
        product.setUser(user);
        productRepository.save(product);
    }
}
