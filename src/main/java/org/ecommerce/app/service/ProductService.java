package org.ecommerce.app.service;

import org.ecommerce.app.model.Product;
import org.ecommerce.app.repo.IProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepo repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }
}
