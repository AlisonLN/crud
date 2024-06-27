package com.example.crud.controllers;

import com.example.crud.domain.product.Product;
import com.example.crud.domain.product.RequestProduct;
import com.example.crud.domain.product.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository repository;
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity getAllProducts (){
        var allProducts = repository.findAllByActive(true);
        return  ResponseEntity.ok(allProducts);
    }

    @PostMapping
    public ResponseEntity registerProduct(@RequestBody @Valid RequestProduct requestProduct) {
        Product newProduct = new Product(requestProduct);
        repository.save(newProduct);
        return ResponseEntity.ok("Product Save");
    }
    @Transactional
    @PutMapping
    public ResponseEntity updateProduct ( @RequestBody @Valid RequestProduct data) {
        Optional<Product> optionalProduct = repository.findById(data.id());
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(data.name());
            product.setPriceInCents(data.priceInCents());
            return ResponseEntity.ok(product);
        } else {
            return ResponseEntity.ok("Product not found");
        }

    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct (@PathVariable String  id) {
      Optional<Product> optionalProduct = repository.findById(id);
      if (optionalProduct.isPresent()){
          Product product = optionalProduct.get();
          product.setActive(false);
          return ResponseEntity.noContent().build();
      }
      else {
          return ResponseEntity.notFound().build();
      }
    }
}
