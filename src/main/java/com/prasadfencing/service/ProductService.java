package com.prasadfencing.service;

import com.prasadfencing.dto.ProductDTO;
import com.prasadfencing.entity.Product;
import com.prasadfencing.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;

    
    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product addProduct(ProductDTO dto) {
        Product product = new Product();
        mapToEntity(dto,product);
        return repository.save(product);
    }

    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found " +id));
    }

    public Product updateProduct(Long id, ProductDTO dto) {
        Product product = getProductById(id);
        mapToEntity(dto,product);
        return repository.save(product);
    }

    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }

    public List<Product> searchProduct(String keyword) {
        return repository.findByNameContainingIgnoreCase(keyword);
    }

    private void mapToEntity(ProductDTO dto, Product product)
    {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setCategory(dto.getCategory());
        product.setImageUrl(dto.getImageUrl());
    }
}
