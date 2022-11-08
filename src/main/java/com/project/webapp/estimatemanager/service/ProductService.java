package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.models.Product;
import com.project.webapp.estimatemanager.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

//TODO: inserire tutti i try catch
@Service
@Transactional
public class ProductService {
    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product addProduct(Product product) {
        return productRepo.save(product);
    }

    public List<Product> findAllProducts() {
        return productRepo.findAll();
    }

    public Product updateProduct(Product product) {
        return productRepo.save(product);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepo.findProductById(id);
    }

    public Optional<Product> findProductByName(String name) {
        return productRepo.findProductByName(name);
    }

    public void deleteProduct(Long id) {
        productRepo.deleteProductById(id);
    }
}
