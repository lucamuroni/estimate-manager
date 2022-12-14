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
import java.util.NoSuchElementException;
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
            if (productRepo.findProductByName(productDto.getName()).isPresent())
                throw new NameAlreadyTakenException("Nome prodotto non disponibile");
            else {
                Product product = new Product();
                product.setName(productDto.getName());
                product.setImageUrl(productDto.getImageUrl());
                Optional<Product> savedProduct = Optional.of(productRepo.save(product));
                return savedProduct
                        .stream()
                        .map(source -> modelMapper.map(source, ProductDto.class))
                        .findFirst()
                        .orElseThrow();
            }
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    public ProductDto updateProduct(ProductDto productDto) throws Exception {
        try {
            Product product = productRepo.findProductById(productDto.getId()).orElseThrow();
            Product modifiedProduct = this.saveChanges(productDto, product);
            productRepo.save(modifiedProduct);
            return productRepo.findProductById(modifiedProduct.getId()).stream()
                    .map(source -> modelMapper.map(source, ProductDto.class))
                    .findFirst()
                    .orElseThrow();
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Elemento non trovato");
        } catch (NullPointerException e) {
            throw new NoSuchElementException("Nessun elemento nella lista");
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
            throw new Exception("Problema sconosciuto");
        }
    }

    public ProductDto findProductById(Long id) throws Exception {
        try {
            Optional<Product> product = productRepo.findProductById(id);
            if (product.isPresent())
                return product
                        .stream()
                        .map(source -> modelMapper.map(source, ProductDto.class))
                        .findFirst()
                        .get();
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

//    public ProductDto findProductByName(String name) throws Exception {
//        try {
//            Optional<Product> product = productRepo.findProductByName(name);
//            if (product.isPresent())
//                return product
//                        .stream()
//                        .map(source -> modelMapper.map(source, ProductDto.class))
//                        .findFirst()
//                        .get();
//            else
//                throw new NoSuchElementException("Nessun elemento trovato");
//        } catch (NoSuchElementException e) {
//            throw new NoSuchElementException(e.getMessage());
//        } catch (Exception e) {
//            throw new Exception("Problema sconosciuto");
//        }
//    }

    public void deleteProduct(Long id) throws Exception {
        try {
            if (productRepo.findProductById(id).isPresent())
                productRepo.deleteProductById(id);
            else
                throw new NoSuchElementException("Nessun elemento trovato");
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new Exception("Problema sconosciuto");
        }
    }

    private Product saveChanges(ProductDto productDto, Product product) throws Exception {
        try {
            if (!productDto.getName().equals(product.getName())) {
                if (productRepo.findProductByName(productDto.getName()).isPresent())
                    throw new NameAlreadyTakenException("Prodotto con quel nome già esistente");
                else
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
