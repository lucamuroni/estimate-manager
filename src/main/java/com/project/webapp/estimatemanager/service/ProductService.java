package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.models.Product;
import com.project.webapp.estimatemanager.repository.ProductRepo;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public ProductService(ProductRepo productRepo, ModelMapper modelMapper) {
        this.productRepo = productRepo;
        this.modelMapper = modelMapper;
    }

    public ProductDto addProduct(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setImageUrl(productDto.getImageUrl());
        productRepo.save(product);
        return productRepo.findProductById(product.getId()).stream()
                .map(source -> modelMapper.map(source, ProductDto.class))
                .findFirst()
                .get();
    }

    public List<ProductDto> findAllProducts() {
        List<Product> products = productRepo.findAll();
        return products.stream()
                .map(source -> modelMapper.map(source, ProductDto.class))
                .toList();
    }

    public Product updateProduct(Product product) {
        return productRepo.save(product);
    }

    public Optional<ProductDto> findProductById(Long id) {
        Optional<Product> product = productRepo.findProductById(id);
        return product.stream()
                .map(source -> modelMapper.map(source, ProductDto.class))
                .findFirst();
    }

    public Optional<ProductDto> findProductByName(String name) {
        Optional<Product> product = productRepo.findProductByName(name);
        return product.stream()
                .map(source -> modelMapper.map(source, ProductDto.class))
                .findFirst();
    }

    public void deleteProduct(Long id) {
        productRepo.deleteProductById(id);
    }
}
