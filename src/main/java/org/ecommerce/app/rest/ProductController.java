package org.ecommerce.app.rest;

import org.ecommerce.app.model.Product;
import org.ecommerce.app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {

    @Autowired
    private IProductService service;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProduct() {
        List<Product> products = service.getAllProducts();
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }
}
