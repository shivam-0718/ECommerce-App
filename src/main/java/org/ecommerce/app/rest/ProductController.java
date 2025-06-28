package org.ecommerce.app.rest;

import org.ecommerce.app.model.Product;
import org.ecommerce.app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
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

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") int id) {
        Product product = service.fetchProduct(id);
        return new ResponseEntity<Product>(product, HttpStatus.OK);
    }

    @PostMapping("/add-product")
    public ResponseEntity<String> addProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        String response = service.addProduct(product, image);
        return new ResponseEntity<String>(response, HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}/image")
    public ResponseEntity<byte[]> getProductImage(@PathVariable("id") int id) {
        Product product = service.fetchProduct(id);
        byte[] imageData = product.getImageData();
        return new ResponseEntity<byte[]>(imageData, HttpStatus.OK);
    }
}
