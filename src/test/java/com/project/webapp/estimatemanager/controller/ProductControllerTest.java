package com.project.webapp.estimatemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webapp.estimatemanager.dtos.ProductDto;
import com.project.webapp.estimatemanager.service.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto productDto;

    @BeforeEach
    public void init() {
        productDto = ProductDto.builder().id(1L).name("Computer").imageUrl("test").build();
    }

    @Test
    public void ProductController_AddProduct_ReturnsCreated() throws Exception {
        given(productService.addProduct(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/product/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_GetAllProducts_ReturnResponseDto() throws Exception {
        List<ProductDto> products = new ArrayList<>();
        products.add(productDto);

        when(productService.findAllProducts()).thenReturn(products);

        ResultActions response = mockMvc.perform(get("/product/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(products.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_GetProductById_ReturnsProductDto() throws Exception {
        when(productService.findProductById(1L)).thenReturn(productDto);

        ResultActions response = mockMvc.perform(get("/product/find/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_UpdateProduct_ReturnsProductDto() throws Exception {
        when(productService.updateProduct(Mockito.any(ProductDto.class))).thenReturn(productDto);

        ResultActions response = mockMvc.perform(put("/product/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(productDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void ProductController_DeleteProduct_ReturnsResponse() throws Exception {
        doNothing().when(productService).deleteProduct(Mockito.any(Long.class));

        ResultActions response = mockMvc.perform(delete("/product/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
