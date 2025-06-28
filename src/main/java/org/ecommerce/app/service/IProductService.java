package org.ecommerce.app.service;

import org.ecommerce.app.model.Product;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IProductService {
    List<Product> getAllProducts();
    Product fetchProduct(int id);
    String addProduct(Product product, MultipartFile image);
    String updateProduct(Product product, MultipartFile image);
    String deleteProduct(int id);
    List<Product> fetchProducts(String keyword);
}
