package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.*;
import com.project.webapp.estimatemanager.models.*;
import com.project.webapp.estimatemanager.repository.EstimateRepo;
import com.project.webapp.estimatemanager.repository.OptionRepo;
import com.project.webapp.estimatemanager.repository.ProductRepo;
import com.project.webapp.estimatemanager.repository.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EstimateServiceTest {
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private EstimateRepo estimateRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private OptionRepo optionRepo;
    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private EstimateService estimateService;

    private Estimate estimate;
    private Set<Opt> opts;
    private Set<Role> employeeRole;
    private Product product;
    private UserEntity client;
    private UserEntity employee;

    private EstimateDto estimateDto;

    @BeforeEach
    public void initEstimateDto() {
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

    @BeforeEach
    public void initEstimate() {
        opts = new HashSet<>();
        Opt opt = Opt.builder().id(1L).name("Sconto").type("2x1").build();
        opts.add(opt);

        Set<Role> clientRole = new HashSet<>();
        Role role1 = Role.builder().id(1L).name("CLIENT").build();
        clientRole.add(role1);

        employeeRole = new HashSet<>();
        Role role2 = Role.builder().id(2L).name("EMPLOYEE").build();
        employeeRole.add(role2);

        client = UserEntity.builder().id(2L).name("prova").email("prova@gmail.com").password("prova").roles(clientRole).build();

        employee = UserEntity.builder().id(1L).name("default").email("default").password("default").roles(employeeRole).build();

        product = Product.builder().id(1L).name("Computer").build();

        estimate = Estimate.builder()
                .id(1L)
                .client(client)
                .employee(employee)
                .product(product)
                .options(opts)
                .price(0F)
                .build();
    }

    @Test
    public void EstimateService_AddEstimate_ReturnsEstimateDto() throws Exception {
        when(userRepo.findUserEntityById(Mockito.any(Long.class))).thenReturn(Optional.of(client));
        when(userRepo.findUserEntityByEmail("default")).thenReturn(Optional.of(employee));
        when(productRepo.findProductById(Mockito.any(Long.class))).thenReturn(Optional.of(product));
        when(optionRepo.findOptById(Mockito.any(Long.class))).thenReturn(Optional.of(opts.stream().findFirst().get()));
        when(estimateRepo.save(Mockito.any(Estimate.class))).thenReturn(estimate);

        EstimateDto savedEstimate = estimateService.addEstimate(estimateDto);

        Assertions.assertThat(savedEstimate).isNotNull();
    }

    @Test
    public void EstimateService_FindAllEstimates_ReturnsAllEstimatesFromDb() throws Exception {
        List<Estimate> estimates = Mockito.mock(List.class);

        when(estimateRepo.findAll()).thenReturn(estimates);

        List<EstimateDto> estimateDtos = estimateService.findAllEstimates();

        Assertions.assertThat(estimateDtos).isNotNull();
    }

    @Test
    public void EstimateService_FindEstimateById_ReturnsEstimateWithThatId() throws Exception {
        when(estimateRepo.findEstimateById(Mockito.any(Long.class))).thenReturn(Optional.of(estimate));

        EstimateDto estimateDto1 = estimateService.findEstimateById(1L);

        Assertions.assertThat(estimateDto1).isNotNull();
    }

    @Test
    public void EstimateService_FindEstimateByUserId_ReturnsUserEstimates() throws Exception {
        List<Estimate> estimates = new ArrayList<>();
        estimates.add(estimate);

        when(userRepo.findUserEntityById(Mockito.any(Long.class))).thenReturn(Optional.of(client));
        when(estimateRepo.findEstimatesByClient(Mockito.any(UserEntity.class))).thenReturn(estimates);

        List<EstimateDto> estimateDtos = estimateService.findEstimatesByUserId(client.getId());

        Assertions.assertThat(estimateDtos).isNotNull();
        Assertions.assertThat(estimateDtos).isNotEmpty();
        Assertions.assertThat(estimateDtos.size()).isEqualTo(estimates.size());
    }

    @Test
    public void OptionService_UpdateOption_ReturnsUpdatedOption() throws Exception {
        estimateDto.setPrice(3F);
        Estimate modifiedEstimate = Estimate.builder()
                .client(client)
                .employee(UserEntity.builder().id(3L).name("employee").email("employee@gmail.com").password("employee").roles(employeeRole).build())
                .product(product)
                .options(opts)
                .price(3F)
                .build();

        when(estimateRepo.findEstimateById(Mockito.any(Long.class))).thenReturn(Optional.of(estimate));
        when(userRepo.findUserEntityById(Mockito.any(Long.class))).thenReturn(Optional.of(employee));
        when(estimateRepo.save(Mockito.any(Estimate.class))).thenReturn(modifiedEstimate);

        EstimateDto savedEstimate = estimateService.updateEstimate(estimateDto);

        Assertions.assertThat(savedEstimate).isNotNull();
    }

    @Test
    public void EstimateService_DeleteEstimate_ReturnsEmptyEstimate() {
        when(estimateRepo.findEstimateById(Mockito.any(Long.class))).thenReturn(Optional.of(estimate));

        assertAll(() -> estimateService.deleteEstimate(1L));
    }
}
