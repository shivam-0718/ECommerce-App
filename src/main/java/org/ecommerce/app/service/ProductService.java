package org.ecommerce.app.service;

import org.ecommerce.app.exception.ProductNotFoundException;
import org.ecommerce.app.model.Product;
import org.ecommerce.app.repo.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
