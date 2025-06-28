package org.ecommerce.app.rest;

import org.ecommerce.app.model.Product;
import org.ecommerce.app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private IProductService service;

    @GetMapping("/products")
    public List<Product> getProduct() {
        return List.of();
    }
}
