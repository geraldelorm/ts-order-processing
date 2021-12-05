package io.turntabl.tsops.ClientConnectivity.controller;

import io.turntabl.tsops.ClientConnectivity.dto.ProductDto;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Product> getAllProduct(){
        return productService.getAllProduct();
    }

    /*@GetMapping(path = "/product/{userId}")
    public List<Product> getUserProduct(@PathVariable("userId")Long userId){
        return productService.getUserProduct(userId);
    }*/

    @GetMapping(path = "/{productId}")
    public Set<Product> getProductsFromPortfolio(@PathVariable("productId")Long productId){
        return productService.getProductsFromPortfolio(productId);
    }

    @PostMapping(path = "create/{portfolioId}")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDto productDto, @PathVariable("portfolioId")Long portfolioId ){
        productService.createProduct(productDto,portfolioId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
