package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.models.Product;
import com.project.webapp.estimatemanager.repository.ProductRepo;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private ProductService productService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Product dbProduct;
    private ProductDto productDto;

    @BeforeEach
    public void init() {
        dbProduct = Product.builder().id(1L).name("Computer").imageUrl("test").build();
        productDto = ProductDto.builder().id(1L).name("Computer").imageUrl("test").build();
    }

    @Test
    public void ProductService_AddProduct_ReturnsProductDto() throws Exception {
        when(productRepo.findProductByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(productRepo.save(Mockito.any(Product.class))).thenReturn(dbProduct);

        ProductDto savedProduct = productService.addProduct(productDto);

        Assertions.assertThat(savedProduct).isNotNull();
    }

    @Test
    public void ProductService_FindAllProducts_ReturnsAllProductsFromDb() throws Exception {
        List<Product> products = Mockito.mock(List.class);

        when(productRepo.findAll()).thenReturn(products);

        List<ProductDto> productDtos = productService.findAllProducts();

        Assertions.assertThat(productDtos).isNotNull();
    }

    @Test
    public void ProductService_FindProductById_ReturnsProductWithThatId() throws Exception {
        when(productRepo.findProductById(Mockito.any(Long.class))).thenReturn(Optional.of(dbProduct));

        ProductDto prodDto = productService.findProductById(1L);

        Assertions.assertThat(prodDto).isNotNull();
    }

    @Test
    public void ProductService_UpdateProduct_ReturnsUpdatedProduct() throws Exception {
        productDto.setName("Computer Windows");
        Product modifiedProduct = Product.builder().id(1L).name("Computer Windows").imageUrl("test").build();

        when(productRepo.findProductById(Mockito.any(Long.class))).thenReturn(Optional.of(dbProduct));
        when(productRepo.findProductByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(productRepo.save(Mockito.any(Product.class))).thenReturn(modifiedProduct);

        ProductDto savedProduct = productService.updateProduct(productDto);

        Assertions.assertThat(savedProduct).isNotNull();
    }

    @Test
    public void ProductService_DeleteProduct_ReturnsEmptyProduct() {
        when(productRepo.findProductById(Mockito.any(Long.class))).thenReturn(Optional.of(dbProduct));

        assertAll(() -> productService.deleteProduct(1L));
    }
}
