package io.turntabl.tsops.ClientConnectivity.controller;

import io.turntabl.tsops.ClientConnectivity.dto.ProductDto;
import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.service.AuthService;
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
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProduct(){

        if(authService.isAdmin()) return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping(path = "/user")
    public ResponseEntity<List<Product>> getUserProduct(){

        if(authService.isClient()) return new ResponseEntity<>(productService.getUserProduct(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }

    @GetMapping(path = "/{portfolioId}")
    public ResponseEntity<List<Product>> getProductsFromPortfolio(@PathVariable("portfolioId")Long portfolioId){

        if(authService.isClient()) return new ResponseEntity<>(productService.getProductsFromPortfolio(portfolioId), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping(path = "create/{portfolioId}")
    public ResponseEntity<Void> createProduct(@RequestBody ProductDto productDto, @PathVariable("portfolioId")Long portfolioId ){
        //TODO: get product from the order
        productService.createProduct(productDto,portfolioId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
