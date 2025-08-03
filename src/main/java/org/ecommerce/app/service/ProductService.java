package org.ecommerce.app.service;

import org.ecommerce.app.exception.ProductNotFoundException;
import org.ecommerce.app.model.Product;
import org.ecommerce.app.repo.IProductRepo;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    @Autowired
    private IProductRepo repo;

    @Autowired
    private ChatClient chatClient;

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
        return "Product has been added successfully with id " + product.getId();
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
            return "Product with given id " + id + " has been updated successfully!";
        }
        throw new ProductNotFoundException("Product with given id is not available. Please try again with the correct id.");
    }

    @Override
    public String deleteProduct(int id) {
        Optional<Product> optional = repo.findById(id);
        if(optional.isPresent()){
            repo.deleteById(id);
            return "Product with id " + id + " has been deleted successfully!";
        }
        throw new ProductNotFoundException("Product with given id is not available. Please try again with the correct id.");
    }

    @Override
    public List<Product> fetchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }

    @Override
    public String generateDescription(String name, String category) {
        String description = """
                Write a concise and professional product description for an e-commerce listing.
                
                Product name: {name}
                Category: {category}
                
                Keep it simple, engaging and highlight it's primary features and benefits.
                Avoid technical jargon and make it customer friendly.
                Limit the description to 250 characters maximum.
                
                """;

        PromptTemplate promptTemplate = new PromptTemplate(description);
        Prompt prompt = promptTemplate.create(
                Map.of("name", name,
                        "category", category));

        String response = chatClient.prompt(prompt).call().content();
        return response;
    }
}
