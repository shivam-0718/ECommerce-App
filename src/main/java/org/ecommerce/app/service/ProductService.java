package org.ecommerce.app.service;

import org.ecommerce.app.exception.ProductNotFoundException;
import org.ecommerce.app.model.Product;
import org.ecommerce.app.repo.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepo repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    public Product fetchProduct(int id) {
        Optional<Product> optional = repo.findById(id);
        if(optional.isPresent()){
            return optional.get();
        }
        throw new ProductNotFoundException("Product with given id is not available. Please try again with the correct id.");
    }

    @Override
    public String addProduct(Product product, MultipartFile image) {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        try {
            product.setImageData(image.getBytes()); //While giving the image bytes, it might throw an Exception
        } catch (IOException e) {
            throw new RuntimeException("Image not correctly uploaded. Please try again!");
        }
        Product addedProduct = repo.save(product);
        return "Product added successfully!";
    }

    @Override
    public String updateProduct(Product product, MultipartFile image) {
        int id = product.getId();
        Optional<Product> optional = repo.findById(id);
        if(optional.isPresent()) {
            product.setImageName(image.getOriginalFilename());
            product.setImageType(image.getContentType());
            try {
                product.setImageData(image.getBytes()); //While giving the image bytes, it might throw an Exception
            } catch (IOException e) {
                throw new RuntimeException("Image not correctly uploaded. Please try again!");
            }
            repo.save(product);
            return "Product updated successfully!";
        }
        throw new ProductNotFoundException("Product with given id is not available. Please try again with the correct id.");
    }
}
