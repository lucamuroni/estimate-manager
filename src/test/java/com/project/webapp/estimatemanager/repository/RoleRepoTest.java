package com.project.webapp.estimatemanager.repository;

import com.project.webapp.estimatemanager.models.Role;
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
public class RoleRepoTest {
    @Autowired
    private RoleRepo roleRepo;

    private Role role;

    @BeforeEach
    public void init() {
        role = Role.builder().name("ADMIN").build();
    }

    @Test
    public void RoleRepo_SaveAll_ReturnsSavedRole() {
        //Act
        Role savedRole = roleRepo.save(role);
        //Assert
        Assertions.assertThat(savedRole).isNotNull();
        Assertions.assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void RoleRepo_GetAll_ReturnsAllRolesFromDb() {
        //Arrange
        Role role2 = Role.builder().name("USER").build();
        //Act
        roleRepo.save(role);
        roleRepo.save(role2);
        List<Role> roles = roleRepo.findAll();
        //Assert
        Assertions.assertThat(roles).isNotNull();
        Assertions.assertThat(roles).isNotEmpty();
        Assertions.assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    public void RoleRepo_FindById_ReturnsRoleWithThatId() {
        //Act
        roleRepo.save(role);
        Role foundRole = roleRepo.findRoleById(role.getId()).get();
        //Assert
        Assertions.assertThat(foundRole).isNotNull();
    }

    @Test
    public void RoleRepo_FindByName_ReturnsRoleWithThatName() {
        //Act
        roleRepo.save(role);
        Role foundRole = roleRepo.findRoleByName(role.getName()).get();
        //Assert
        Assertions.assertThat(foundRole).isNotNull();
    }

    @Test
    public void RoleRepo_UpdateRole_ReturnUpdatedRole() {
        //Act
        roleRepo.save(role);
        Role savedRole = roleRepo.findRoleById(role.getId()).get();
        savedRole.setName("USER");
        Role updatedRole = roleRepo.save(savedRole);
        //Assert
        Assertions.assertThat(updatedRole).isNotNull();
        Assertions.assertThat(updatedRole.getName()).isEqualTo(savedRole.getName());
    }

    @Test
    public void RoleRepo_DeleteRole_ReturnRoleIsEmpty() {
        //Act
        roleRepo.save(role);
        roleRepo.deleteRoleById(role.getId());
        Optional<Role> roleReturn = roleRepo.findRoleById(role.getId());
        //Assert
        Assertions.assertThat(roleReturn).isEmpty();
    }
}
