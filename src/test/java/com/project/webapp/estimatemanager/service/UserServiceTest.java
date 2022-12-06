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
//import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepo userRepo;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepo roleRepo;
    @InjectMocks
    private UserService userService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void UserService_AddUser_ReturnsUserDto() throws Exception {
        Set<RoleDto> roles = new HashSet<>();
        roles.add(new RoleDto(1L, "client"));

        UserEntity user = UserEntity.builder().name("test").email("test@gmail.com").password("tests").build();
        UserDto userDto = UserDto.builder().name("test").email("test@gmail.com").password("test").roles(roles).build();
        List<Role> dbRoles = new ArrayList<>();
        Role role = new Role();
        role.setName("client");
        role.setId(1L);
        dbRoles.add(role);

        when(userRepo.save(Mockito.any(UserEntity.class))).thenReturn(user);
        //when(userRepo.findUserEntityByEmail(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(roleRepo.save(Mockito.any(Role.class))).thenReturn(role);

        UserDto savedUser = userService.addUser(userDto);
        Assertions.assertThat(savedUser).isNotNull();
    }

}
