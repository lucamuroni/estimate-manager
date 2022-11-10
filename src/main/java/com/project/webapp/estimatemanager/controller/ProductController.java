package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.exception.NameAlreadyTakenException;
import com.project.webapp.estimatemanager.exception.ProductNotFoundException;
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
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<ProductDto> products = productService.findAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/find")
    public ResponseEntity<ProductDto> getProductById(@RequestParam(name = "id") Long id) throws ProductNotFoundException {
        if (productService.findProductById(id).isEmpty()) {
            throw new ProductNotFoundException("Prodotto assente o id errato");
        }
        ProductDto product = productService.findProductById(id).get();
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) throws NameAlreadyTakenException {
        if (productService.findProductByName(productDto.getName()).isPresent())
            throw new NameAlreadyTakenException("Nome prodotto non disponibile");
        ProductDto newProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) throws ProductNotFoundException, NameAlreadyTakenException {
        if (productService.findProductById(productDto.getId()).isEmpty()) {
            throw new ProductNotFoundException("Prodotto assente o id errato");
        }
        ProductDto updateProduct;
        try {
            updateProduct = productService.updateProduct(productDto);
        } catch (Exception e) {
            throw new NameAlreadyTakenException(e.getMessage());
        }
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<?> deleteProduct(@RequestParam(name = "id") Long id) throws ProductNotFoundException {
        if (productService.findProductById(id).isEmpty()) {
            throw new ProductNotFoundException("Prodotto assente o id errato");
        }
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
