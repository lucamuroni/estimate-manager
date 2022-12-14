package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EstimateRepoTest {
    @Autowired
    private EstimateRepo estimateRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private OptionRepo optionRepo;
    @Autowired
    private RoleRepo roleRepo;

    private Estimate estimate;
    private Estimate estimate2;
    private Set<Opt> opts;
    private Set<Opt> opts2;
    private Set<Role> clientRole;
    private Set<Role> employeeRole;
    private Product product;
    private UserEntity client;
    private UserEntity employee;

    @BeforeEach
    public void initOptions() {
        opts = new HashSet<>();
        Opt opt = Opt.builder().name("Sconto").type("2x1").build();
        optionRepo.save(opt);
        opts.add(opt);
        opts2 = new HashSet<>();
        Opt opt2 = Opt.builder().name("Sconticino").type("5%").build();
        optionRepo.save(opt2);
        opts2.add(opt2);
    }

    //@BeforeEach
    //public void initProduct() {
        //product = Product.builder().name("Computer").build();
        //productRepo.save(product);
    //}

    @BeforeEach
    public void initRoles() {
        clientRole = new HashSet<>();
        Role role1 = Role.builder().name("CLIENT").build();
        roleRepo.save(role1);
        clientRole.add(role1);
        employeeRole = new HashSet<>();
        Role role2 = Role.builder().name("EMPLOYEE").build();
        roleRepo.save(role2);
        employeeRole.add(role2);
    }

    @BeforeEach
    public void initUsers() {
        //Set<UserEntity> clients = new HashSet<>();
        client = UserEntity.builder().name("prova").email("prova@gmail.com").password("prova").roles(clientRole).build();
        userRepo.save(client);
        //clients.add(client);
        //clientRole.stream().findFirst().get().setUsers(clients);
        //Set<UserEntity> employees = new HashSet<>();
        employee = UserEntity.builder().name("default").email("default").password("default").roles(employeeRole).build();
        userRepo.save(employee);
        //employees.add(employee);
        //employeeRole.stream().findFirst().get().setUsers(employees);
    }

    @BeforeEach
    public void initEstimateAndProduct() {
        product = Product.builder().name("Computer").build();
        productRepo.save(product);
        estimate = Estimate.builder()
                .client(client)
                .employee(employee)
                .product(product)
                .options(opts)
                .price(0F)
                .build();
        estimate2 = Estimate.builder()
                .client(client)
                .employee(employee)
                .product(product)
                .options(opts2)
                .price(0F)
                .build();
    }

    @Test
    public void EstimateRepo_SaveAll_ReturnsSavedEstimate() {
        Estimate savedEstimate = estimateRepo.save(estimate);

        Assertions.assertThat(savedEstimate).isNotNull();
        Assertions.assertThat(savedEstimate.getId()).isGreaterThan(0);
    }

    @Test
    public void EstimateRepo_GetAll_ReturnsAllEstimatesFromDb() {
        estimateRepo.save(estimate);
        estimateRepo.save(estimate2);

        List<Estimate> estimates = estimateRepo.findAll();

        Assertions.assertThat(estimates).isNotNull();
        Assertions.assertThat(estimates).isNotEmpty();
        Assertions.assertThat(estimates.size()).isEqualTo(2);
    }

    @Test
    public void EstimateRepo_FindById_ReturnsEstimateWithThatId() {
        estimateRepo.save(estimate);

        Estimate foundEstimate = estimateRepo.findEstimateById(estimate.getId()).get();

        Assertions.assertThat(foundEstimate).isNotNull();
    }

    @Test
    public void EstimateRepo_FindByClient_ReturnsClientEstimates() {
        estimateRepo.save(estimate);

        List<Estimate> estimates = estimateRepo.findEstimatesByClient(client);

        Assertions.assertThat(estimates).isNotNull();
        Assertions.assertThat(estimates).isNotEmpty();
        Assertions.assertThat(estimates.size()).isEqualTo(1);
    }

    @Test
    public void EstimateRepo_FindByEmployee_ReturnsEmployeeEstimates() {
        estimateRepo.save(estimate);

        List<Estimate> estimates = estimateRepo.findEstimatesByEmployee(employee);

        Assertions.assertThat(estimates).isNotNull();
        Assertions.assertThat(estimates).isNotEmpty();
        Assertions.assertThat(estimates.size()).isEqualTo(1);
    }

    @Test
    public void OptionRepo_UpdateOption_ReturnUpdatedOption() {
        Estimate savedEstimate = estimateRepo.save(estimate);

        savedEstimate.setOptions(opts2);
        Estimate updatedEstimate = estimateRepo.save(savedEstimate);

        Assertions.assertThat(updatedEstimate).isNotNull();
        Assertions.assertThat(updatedEstimate.getOptions().size()).isEqualTo(savedEstimate.getOptions().size());
    }

    @Test
    public void EstimateRepo_DeleteEstimate_ReturnEstimateIsEmpty() {
        estimateRepo.save(estimate);

        estimateRepo.deleteEstimateById(estimate.getId());
        Optional<Estimate> estimateReturn = estimateRepo.findEstimateById(estimate.getId());

        Assertions.assertThat(estimateReturn).isEmpty();
    }
}
