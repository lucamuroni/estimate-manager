package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<List<ProductDto>> getAllProducts() throws GenericException {
        try {
            List<ProductDto> products = productService.findAllProducts();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            ProductDto product = productService.findProductById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) throws NameAlreadyTakenException, NoSuchElementException, GenericException {
        try {
            ProductDto newProduct = productService.addProduct(productDto);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) throws NoSuchElementException, NameAlreadyTakenException, GenericException {
        try {
            ProductDto updateProduct = productService.updateProduct(productDto);
            return new ResponseEntity<>(updateProduct, HttpStatus.OK);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) throws NoSuchElementException, GenericException {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
