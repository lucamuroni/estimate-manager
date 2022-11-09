package com.project.webapp.estimatemanager.controller;

import com.project.webapp.estimatemanager.dtos.ProductDto;
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
    public ResponseEntity<ProductDto> getProductById(@RequestParam(name = "id") Long id) {
        if (productService.findProductById(id).isPresent()) {
            ProductDto product = productService.findProductById(id).get();
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody ProductDto productDto) {
        if (productService.findProductByName(productDto.getName()).isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        ProductDto newProduct = productService.addProduct(productDto);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto) {
        if (productService.findProductById(productDto.getId()).isPresent()) {
            ProductDto updateProduct = productService.updateProduct(productDto);
            return new ResponseEntity<>(updateProduct, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    //@DeleteMapping(value = "/delete")
    //public ResponseEntity<?> deleteProduct(@RequestParam(name = "id") Long id) {
        //if (productService.findProductById(id).isPresent()) {
            //productService.deleteProduct(id);
            //return new ResponseEntity<>(HttpStatus.OK);
        //}
        //return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //}
}
