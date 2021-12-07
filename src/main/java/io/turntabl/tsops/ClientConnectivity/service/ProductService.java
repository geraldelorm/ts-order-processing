package io.turntabl.tsops.ClientConnectivity.service;

import io.turntabl.tsops.ClientConnectivity.dto.ProductDto;
import io.turntabl.tsops.ClientConnectivity.entity.Portfolio;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.entity.User;
import io.turntabl.tsops.ClientConnectivity.repository.PortfolioRepository;
import io.turntabl.tsops.ClientConnectivity.repository.ProductRepository;
import io.turntabl.tsops.ClientConnectivity.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Product>> getAllProduct(){
        if(authService.isAdmin()){
            return new ResponseEntity<>(productRepository.findAll(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //get all user's products
    public ResponseEntity<List<Product>> getUserProduct(){
        User user = authService.getCurrentUser();
        if(authService.isClient()){
            return new ResponseEntity<>(user.getProducts(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //get products from a specific portfolio
    public ResponseEntity<List<Product>> getProductsFromPortfolio(Long portfolioId){
        Portfolio portfolio = portfolioRepository.getById(portfolioId);
        if(authService.isClient()){
            return new ResponseEntity<>(portfolio.getProducts(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

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
