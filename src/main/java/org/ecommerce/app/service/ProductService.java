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

    @Autowired
    private AiImageGeneratorService aiImageGenService;

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

    @Override
    public byte[] generateImage(String name, String category, String description) {
        String imagePrompt = """
                Generate a highly realistic, professional-grade e-commerce product image.
                
                    Product details:
                        - Product name: {name}
                        - Product category: {category}
                        - Product description: {description}
                    
                    Requirements:
                         - Use a clean, minimalistic, white or very light grey background.
                         - Ensure the product is well-lit with soft, natural-looking lighting.
                         - Add realistic shadows and soft reflections to ground the product naturally.
                         - No humans, brand logos, watermarks, or text overlays should be visible.
                         - Showcase the product from its most flattering angle that highlights key features.
                         - Ensure the product occupies a prominent position in the frame, centered or slightly off-centered.
                         - Maintain a high resolution and sharpness, ensuring all textures, colors, and details are clear.
                         - Follow the typical visual style of top e-commerce websites like Amazon, Flipkart, or Shopify.
                         - Make the product appear life-like and professionally photographed in a studio setup.
                         - The final image should look immediately ready for use on an e-commerce website without further editing.
                """;

        PromptTemplate promptTemplate = new PromptTemplate(imagePrompt);
        Prompt prompt = promptTemplate.create(
                Map.of("name", name,
                        "category", category,
                        "description", description));

        return aiImageGenService.generateImage(imagePrompt);
    }
}
