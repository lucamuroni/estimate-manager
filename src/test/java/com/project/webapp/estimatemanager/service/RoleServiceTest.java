package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.models.Role;
import com.project.webapp.estimatemanager.repository.RoleRepo;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
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
public class RoleServiceTest {
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private RoleRepo roleRepo;
    @InjectMocks
    private RoleService roleService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void RoleService_AddRole_ReturnsRoleDto() throws Exception {
        //Role used to mock the save method
        Role role = Role.builder().id(1L).name("CLIENT").build();
        //Role used to mock the dto passed with the request
        RoleDto roleDto = RoleDto.builder().name("CLIENT").build();
        //Mocking repo methods called by addUser
        when(roleRepo.findRoleByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(roleRepo.save(Mockito.any(Role.class))).thenReturn(role);
        //Calling the real method
        RoleDto savedRole = roleService.addRole(roleDto);
        //Assertions
        Assertions.assertThat(savedRole).isNotNull();
    }

    @Test
    public void RoleService_FindAllRoles_ReturnsAllRolesFromDb() throws Exception {
        //Creation of the list used to mock the method
        List<Role> roles = Mockito.mock(List.class);
        //Mocking repo methods called by findAllRoles
        when(roleRepo.findAll()).thenReturn(roles);
        //Calling the real method
        List<RoleDto> roleDtos = roleService.findAllRoles();
        //Assertions
        Assertions.assertThat(roleDtos).isNotNull();
    }

    @Test
    public void RoleService_FindRoleById_ReturnsRoleWithThatId() throws Exception {
        //Creation of the role used to mock the method
        Role role = Role.builder().id(1L).name("CLIENT").build();
        //Mocking repo methods called by findRoleById
        when(roleRepo.findRoleById(Mockito.any(Long.class))).thenReturn(Optional.of(role));
        //Calling the real method
        RoleDto roleDto = roleService.findRoleById(1L);
        //Assertions
        Assertions.assertThat(roleDto).isNotNull();
    }

    @Test
    public void RoleService_UpdateRole_ReturnsUpdatedRole() throws Exception {
        //Creation of the roles used to mock the method
        Role dbRole = Role.builder().id(1L).name("CLIENT").build();
        RoleDto roleDto = RoleDto.builder().id(1L).name("CUSTOMER").build();
        Role modifiedRole = Role.builder().id(1L).name("CUSTOMER").build();
        //Mocking repo methods called by updateRole
        when(roleRepo.findRoleById(Mockito.any(Long.class))).thenReturn(Optional.of(dbRole));
        when(roleRepo.findRoleByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(roleRepo.save(Mockito.any(Role.class))).thenReturn(modifiedRole);
        //Calling the real method
        RoleDto savedRole = roleService.updateRole(roleDto);
        //Assertions
        Assertions.assertThat(savedRole).isNotNull();
    }

    @Test
    public void RoleService_DeleteRole_ReturnsEmptyRole() {
        //Creation of the role used to mock the method
        Role role = Role.builder().id(1L).name("CLIENT").build();
        //Mocking repo methods called by deleteRole
        when(roleRepo.findRoleById(Mockito.any(Long.class))).thenReturn(Optional.of(role));
        //Assertions
        assertAll(() -> roleService.deleteRole(1L));
    }
}
