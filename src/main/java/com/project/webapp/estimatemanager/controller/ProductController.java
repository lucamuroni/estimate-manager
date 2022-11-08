package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.models.Product;
import com.project.webapp.estimatemanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO: inserire tutti i try catch
@RestController
@RequestMapping(value = "/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<Product> getProductById(@RequestParam(name = "id") Long id) {
        if (productService.findProductById(id).isPresent()) {
            Product product = productService.findProductById(id).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        if (productService.findProductByName(product.getName()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        Product newProduct = productService.addProduct(product);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Product> updateProduct(@RequestBody Product product) {
        if (productService.findProductById(product.getId()).isPresent()) {
            Product updateProduct = productService.updateProduct(product);
            return new ResponseEntity<>(updateProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam(name = "id") Long id) {
        if (productService.findProductById(id).isPresent()) {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
