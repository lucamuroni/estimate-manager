package com.project.webapp.estimatemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webapp.estimatemanager.dtos.*;
import com.project.webapp.estimatemanager.service.EstimateService;
import com.project.webapp.estimatemanager.service.UserService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = EstimateController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class EstimateControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EstimateService estimateService;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private EstimateDto estimateDto;

    @BeforeEach
    public void init() {
        Set<OptDto> optDtos = new HashSet<>();
        OptDto optDto = OptDto.builder().id(1L).name("Sconto").type("2x1").build();
        optDtos.add(optDto);

        Set<RoleDto> clientDtoRoles = new HashSet<>();
        RoleDto roleDto1 = RoleDto.builder().id(1L).name("CLIENT").build();
        clientDtoRoles.add(roleDto1);

        Set<RoleDto> employeeDtoRoles = new HashSet<>();
        RoleDto roleDto2 = RoleDto.builder().id(2L).name("EMPLOYEE").build();
        employeeDtoRoles.add(roleDto2);

        UserDto clientDto = UserDto.builder().id(2L).name("prova").email("prova@gmail.com").password("prova").roles(clientDtoRoles).build();

        UserDto employeeDto = UserDto.builder().id(1L).name("default").email("default").password("default").roles(employeeDtoRoles).build();

        ProductDto productDto = ProductDto.builder().id(1L).name("Computer").build();

        estimateDto = EstimateDto.builder()
                .id(1L)
                .client(clientDto)
                .employee(employeeDto)
                .product(productDto)
                .options(optDtos)
                .price(0F)
                .build();
    }

    @Test
    public void EstimateController_AddEstimate_ReturnsCreated() throws Exception {
        given(estimateService.addEstimate(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/estimate/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estimateDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.name", CoreMatchers.is(estimateDto.getClient().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.name", CoreMatchers.is(estimateDto.getEmployee().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.name", CoreMatchers.is(estimateDto.getProduct().getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void EstimateController_GetAllEstimates_ReturnResponseDto() throws Exception {
        List<EstimateDto> estimates = new ArrayList<>();
        estimates.add(estimateDto);

        when(estimateService.findAllEstimates()).thenReturn(estimates);

        ResultActions response = mockMvc.perform(get("/estimate/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(estimates.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void EstimateController_GetEstimateById_ReturnsEstimateDto() throws Exception {
        when(estimateService.findEstimateById(1L)).thenReturn(estimateDto);

        ResultActions response = mockMvc.perform(get("/estimate/find/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estimateDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.name", CoreMatchers.is(estimateDto.getClient().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.name", CoreMatchers.is(estimateDto.getEmployee().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.name", CoreMatchers.is(estimateDto.getProduct().getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void EstimateController_UpdateEstimate_ReturnsEstimateDto() throws Exception {
        when(estimateService.updateEstimate(Mockito.any(EstimateDto.class))).thenReturn(estimateDto);

        ResultActions response = mockMvc.perform(put("/estimate/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estimateDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.client.name", CoreMatchers.is(estimateDto.getClient().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employee.name", CoreMatchers.is(estimateDto.getEmployee().getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.product.name", CoreMatchers.is(estimateDto.getProduct().getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void EstimateController_DeleteEstimate_ReturnsResponse() throws Exception {
        doNothing().when(estimateService).deleteEstimate(Mockito.any(Long.class));

        ResultActions response = mockMvc.perform(delete("/estimate/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
