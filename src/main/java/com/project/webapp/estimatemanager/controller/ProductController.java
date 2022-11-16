package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.exception.GenericException;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.exception.ProductNotFoundException;
import com.project.webapp.estimatemanager.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/find")
    public ResponseEntity<ProductDto> getProductById(@RequestParam(name = "id") Long id) throws ProductNotFoundException, GenericException {
        try {
            if (productService.findProductById(id).isEmpty()) {
                throw new ProductNotFoundException("Prodotto assente o id errato");
            }
            ProductDto product = productService.findProductById(id).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) throws NameAlreadyTakenException, GenericException {
        try {
            if (productService.findProductByName(productDto.getName()).isPresent())
                throw new NameAlreadyTakenException("Nome prodotto non disponibile");
            ProductDto newProduct = productService.addProduct(productDto);
            return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) throws ProductNotFoundException, NameAlreadyTakenException, GenericException {
        try {
            if (productService.findProductById(productDto.getId()).isEmpty()) {
                throw new ProductNotFoundException("Prodotto assente o id errato");
            }
            ProductDto updateProduct;
            updateProduct = productService.updateProduct(productDto);
            return new ResponseEntity<>(updateProduct, HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException(e.getMessage());
        } catch (NameAlreadyTakenException e) {
            throw new NameAlreadyTakenException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam(name = "id") Long id) throws ProductNotFoundException, GenericException {
        try {
            if (productService.findProductById(id).isEmpty()) {
                throw new ProductNotFoundException("Prodotto assente o id errato");
            }
            productService.deleteProduct(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ProductNotFoundException e) {
            throw new ProductNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new GenericException(e.getMessage());
        }
    }
}
