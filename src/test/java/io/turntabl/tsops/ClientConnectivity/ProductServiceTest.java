package io.turntabl.tsops.ClientConnectivity;

import io.turntabl.tsops.ClientConnectivity.entity.Product;
import io.turntabl.tsops.ClientConnectivity.repository.ProductRepository;
import io.turntabl.tsops.ClientConnectivity.service.ProductService;
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
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    Product product1 = new Product();
    Product product2 = new Product();
    Product product3 = new Product();

    List<Product> productList = List.of(product1, product2, product3);

    @Test
    void getAllProduct() {
        when(productRepository.findAll()).thenReturn(productList);
        List<Product> products = productService.getAllProduct();
        assertEquals(3, products.size());
    }

    @Test
    void getProductsFromPortfolio() {
    }

    @Test
    void createProduct() {
    }
}