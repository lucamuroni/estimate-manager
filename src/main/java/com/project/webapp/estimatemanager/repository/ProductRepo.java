package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findProductById(Long id);
    Optional<Product> findProductByName(String name);
    void deleteProductById(Long id);
}
