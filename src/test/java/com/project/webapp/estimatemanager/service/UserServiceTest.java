package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.dtos.UserDto;
import com.project.webapp.estimatemanager.models.Role;
import com.project.webapp.estimatemanager.models.UserEntity;
import com.project.webapp.estimatemanager.repository.RoleRepo;
import com.project.webapp.estimatemanager.repository.UserRepo;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private RoleRepo roleRepo;
    @InjectMocks
    private UserService userService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void UserService_AddUser_ReturnsUserDto() throws Exception {
        //Roles coming from request
        Set<RoleDto> roles = new HashSet<>();
        roles.add(new RoleDto(1L, "client"));
        //Roles from db
        Role role = new Role();
        role.setName("client");
        role.setId(1L);
        List<Role> dbRoles = new ArrayList<>();
        dbRoles.add(role);
        //Roles used to mock
        Set<Role> savedRoles = new HashSet<>();
        savedRoles.add(role);
        //User used to mock the save method
        UserEntity user = UserEntity.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").roles(savedRoles).client_estimates(new HashSet<>())
                .employee_estimates(new HashSet<>()).build();
        //User used to mock the dto passed with the request
        UserDto userDto = UserDto.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").roles(roles).build();
        //Mocking repo methods called by addUser
        when(userRepo.save(Mockito.any(UserEntity.class))).thenReturn(user);
        when(userRepo.findUserEntityByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(roleRepo.findAll()).thenReturn(dbRoles);
        //Calling the real method
        UserDto savedUser = userService.addUser(userDto);
        //Assertions
        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void UserService_FindAllUsers_ReturnsAllUsersFromDb() throws Exception{
        //Creation of the list used to mock the method
        List<UserEntity> dbUsers = Mockito.mock(List.class);
        //Mocking repo methods called by findAllUsers
        when(userRepo.findAll()).thenReturn(dbUsers);
        //Calling the real method
        List<UserDto> savedUser = userService.findAllUsers();
        //Assertions
        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void UserService_FindUserById_ReturnsUserWithThatId() throws Exception{
        //Creation of the user used to mock the method
        UserEntity user = UserEntity.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").roles(new HashSet<>()).client_estimates(new HashSet<>())
                .employee_estimates(new HashSet<>()).build();
        //Mocking repo methods called by findUserById
        when(userRepo.findUserEntityById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        //Calling the real method
        UserDto userDto = userService.findUserById(1L);
        //Assertions
        Assertions.assertThat(userDto).isNotNull();
    }

    @Test
    public void UserService_FindUserByEmail_ReturnsUserWithThatEmail() throws Exception{
        //Creation of the user used to mock the method
        UserEntity user = UserEntity.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").roles(new HashSet<>()).client_estimates(new HashSet<>())
                .employee_estimates(new HashSet<>()).build();
        //Mocking repo methods called by findUserById
        when(userRepo.findUserEntityByEmail(Mockito.any(String.class))).thenReturn(Optional.of(user));
        //Calling the real method
        UserDto userDto = userService.findUserByEmail("test");
        //Assertions
        Assertions.assertThat(userDto).isNotNull();
    }

    @Test
    public void UserService_UpdateUser_ReturnsUpdatedUser() throws Exception {
        //Creation of the users used to mock the method
        UserEntity dbUser = UserEntity.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").roles(new HashSet<>()).client_estimates(new HashSet<>())
                .employee_estimates(new HashSet<>()).build();
        UserDto userDto = UserDto.builder().id(1L).name("prova").email("test@gmail.com")
                .password("prova").roles(new HashSet<>()).build();
        UserEntity modifiedUser = UserEntity.builder().id(1L).name("prova").email("test@gmail.com")
                .password("prova").roles(new HashSet<>()).client_estimates(new HashSet<>())
                .employee_estimates(new HashSet<>()).build();
        //Mocking repo methods called by updateUser
        when(userRepo.findUserEntityById(Mockito.any(Long.class))).thenReturn(Optional.of(dbUser));
        when(userRepo.save(Mockito.any(UserEntity.class))).thenReturn(modifiedUser);
        //Calling the real method
        UserDto updatedUserDto = userService.updateUser(userDto);
        //Assertions
        Assertions.assertThat(updatedUserDto).isNotNull();
    }

    @Test
    public void UserService_DeleteUser_ReturnsEmptyUser() {
        //Creation of the user used to mock the method
        UserEntity user = UserEntity.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").roles(new HashSet<>()).client_estimates(new HashSet<>())
                .employee_estimates(new HashSet<>()).build();
        //Mocking repo methods called by deleteUser
        when(userRepo.findUserEntityById(Mockito.any(Long.class))).thenReturn(Optional.of(user));
        //Assertions
        assertAll(() -> userService.deleteUser(1L));
    }

}
