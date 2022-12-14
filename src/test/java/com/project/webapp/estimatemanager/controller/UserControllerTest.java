package com.project.webapp.estimatemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.dtos.UserDto;
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

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    private UserDto requestUser;

    @BeforeEach
    public void init() {
        requestUser = UserDto.builder().id(1L).name("test").email("test@gmail.com")
                .password("test").build();
        RoleDto requestRole = RoleDto.builder().id(1L).name("CLIENT").build();
        Set<RoleDto> roleDtos = new HashSet<>();
        roleDtos.add(requestRole);
        requestUser.setRoles(roleDtos);
    }

    @Test
    public void UserController_AddUser_ReturnsCreated() throws Exception {
        //Mocking the method when calling the service
        given(userService.addUser(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));
        //Using MockMvc to mock the response
        ResultActions response = mockMvc.perform(post("/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUser)));
        //Assertions
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(requestUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(requestUser.getPassword())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void UserController_GetAllUsers_ReturnResponseDto() throws Exception {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(requestUser);

        when(userService.findAllUsers()).thenReturn(userDtos);

        ResultActions response = mockMvc.perform(get("/user/all")
                .contentType(MediaType.APPLICATION_JSON));
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(userDtos.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void UserController_GetUserById_ReturnsUserDto() throws Exception {
        //Mocking the method when calling the service
        when(userService.findUserById(1L)).thenReturn(requestUser);
        //Using MockMvc to mock the response
        ResultActions response = mockMvc.perform(get("/user/find/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUser)));
        //Assertions
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(requestUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(requestUser.getPassword())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void UserController_UpdateUser_ReturnsUserDto() throws Exception {
        //Mocking the method when calling the service
        when(userService.updateUser(Mockito.any(UserDto.class))).thenReturn(requestUser);
        //Using MockMvc to mock the response
        ResultActions response = mockMvc.perform(put("/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestUser)));
        //Assertions
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(requestUser.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(requestUser.getPassword())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void UserController_DeleteUser_ReturnsResponse() throws Exception {
        //Mocking the method when calling the service
        doNothing().when(userService).deleteUser(Mockito.any(Long.class));
        //Using MockMvc to mock the response
        ResultActions response = mockMvc.perform(delete("/user/delete/1")
                .contentType(MediaType.APPLICATION_JSON));
        //Assertions
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
