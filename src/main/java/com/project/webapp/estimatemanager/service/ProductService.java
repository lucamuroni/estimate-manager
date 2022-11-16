package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.models.Product;
import com.project.webapp.estimatemanager.repository.ProductRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    public ProductDto addProduct(ProductDto productDto) throws Exception {
        try {
            Product product = new Product();
            product.setName(productDto.getName());
            product.setImageUrl(productDto.getImageUrl());
            productRepo.save(product);
            return productRepo.findProductById(product.getId()).stream()
                    .map(source -> modelMapper.map(source, ProductDto.class))
                    .findFirst()
                    .get();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public ProductDto updateProduct(ProductDto productDto) throws Exception {
        try {
            Product product = productRepo.findProductById(productDto.getId()).get();
            Product modifiedProduct = this.saveChanges(productDto, product);
            productRepo.save(modifiedProduct);
            return productRepo.findProductById(modifiedProduct.getId()).stream()
                    .map(source -> modelMapper.map(source, ProductDto.class))
                    .findFirst()
                    .get();
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (GenericException e) {
            throw new GenericException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public List<ProductDto> findAllProducts() throws Exception {
        try {
            List<Product> products = productRepo.findAll();
            return products.stream()
                    .map(source -> modelMapper.map(source, ProductDto.class))
                    .toList();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Optional<ProductDto> findProductById(Long id) throws Exception {
        try {
            Optional<Product> product = productRepo.findProductById(id);
            return product.stream()
                    .map(source -> modelMapper.map(source, ProductDto.class))
                    .findFirst();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public Optional<ProductDto> findProductByName(String name) throws Exception {
        try {
            Optional<Product> product = productRepo.findProductByName(name);
            return product.stream()
                    .map(source -> modelMapper.map(source, ProductDto.class))
                    .findFirst();
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public void deleteProduct(Long id) throws Exception {
        try {
            productRepo.deleteProductById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private Product saveChanges(ProductDto productDto, Product product) throws Exception {
        try {
            if (!productDto.getName().equals(product.getName())) {
                if (productRepo.findProductByName(productDto.getName()).isPresent()) {
                    throw new NameAlreadyTakenException("Prodotto con quel nome già esistente");
                }
                product.setName(productDto.getName());
            }
            product.setImageUrl(productDto.getImageUrl());
            return product;
        }catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException("Problema sconosciuto");
        }
    }
}
