package org.ecommerce.app.rest;

import org.ecommerce.app.model.Product;
import org.ecommerce.app.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

    @PutMapping("/update-product")
    public ResponseEntity<String> updateProduct(@RequestPart Product product, @RequestPart MultipartFile image) {
        String response = service.updateProduct(product, image);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @DeleteMapping("/delete-product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") int id) {
        String response = service.deleteProduct(id);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> products = service.fetchProducts(keyword);
        System.out.println("Searching.....");
        return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
    }

    @PostMapping("/product/generate-description")
    public ResponseEntity<String> generateDescription(@RequestParam String name, @RequestParam String category) {
        String response = service.generateDescription(name, category);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/product/generate-image")
    public ResponseEntity<byte[]> generateImage(@RequestParam String name, @RequestParam String category, @RequestParam String description) {
        byte[] aiImage = service.generateImage(name, category, description);
        return new ResponseEntity<byte[]>(aiImage, HttpStatus.CREATED);
    }
}
