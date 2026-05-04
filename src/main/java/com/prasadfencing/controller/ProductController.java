package com.prasadfencing.controller;


import com.prasadfencing.dto.ProductDTO;
import com.prasadfencing.entity.Product;
import com.prasadfencing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductDTO dto)
    {
        return ResponseEntity.ok(productService.addProduct(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable Long id)
    {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id,@RequestBody ProductDTO dto){
        return ResponseEntity.ok(productService.updateProduct(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id)
    {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Deleted SuccessFully");
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(@RequestParam String keyword)
    {
        return ResponseEntity.ok(productService.searchProduct(keyword));
    }
}
