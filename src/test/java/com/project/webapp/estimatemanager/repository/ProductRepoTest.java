package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProductRepoTest {
    @Autowired
    private ProductRepo productRepo;

    private Product product;

    @BeforeEach
    public void ProductRepo_SaveAll_ReturnsSavedProduct() {
        product = Product.builder().name("Computer").build();
    }

    @Test
    public void RoleRepo_SaveAll_ReturnsSavedRole() {
        Product savedProduct = productRepo.save(product);

        Assertions.assertThat(savedProduct).isNotNull();
        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void ProductRepo_GetAll_ReturnsAllProductsFromDb() {

        Product product2 = Product.builder().name("Mouse").build();

        productRepo.save(product);
        productRepo.save(product2);
        List<Product> products = productRepo.findAll();

        Assertions.assertThat(products).isNotNull();
        Assertions.assertThat(products).isNotEmpty();
        Assertions.assertThat(products.size()).isEqualTo(2);
    }

    @Test
    public void ProductRepo_FindById_ReturnsProductWithThatId() {

        productRepo.save(product);
        Product foundProduct = productRepo.findProductById(product.getId()).get();

        Assertions.assertThat(foundProduct).isNotNull();
    }

    @Test
    public void RoleRepo_FindByName_ReturnsRoleWithThatName() {
        productRepo.save(product);
        Product foundProduct = productRepo.findProductByName(product.getName()).get();

        Assertions.assertThat(foundProduct).isNotNull();
    }

    @Test
    public void ProductRepo_UpdateProduct_ReturnUpdatedProduct() {
        productRepo.save(product);
        Product savedProduct = productRepo.findProductById(product.getId()).get();
        savedProduct.setName("Keyboard");
        Product updatedProduct = productRepo.save(savedProduct);

        Assertions.assertThat(updatedProduct).isNotNull();
        Assertions.assertThat(updatedProduct.getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    public void ProductRepo_DeleteProduct_ReturnProductIsEmpty() {
        //Act
        productRepo.save(product);
        productRepo.deleteProductById(product.getId());
        Optional<Product> productReturn = productRepo.findProductById(product.getId());
        //Assert
        Assertions.assertThat(productReturn).isEmpty();
    }
}
