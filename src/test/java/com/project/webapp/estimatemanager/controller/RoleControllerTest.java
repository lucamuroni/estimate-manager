package com.project.webapp.estimatemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webapp.estimatemanager.dtos.RoleDto;
import com.project.webapp.estimatemanager.service.RoleService;
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

@WebMvcTest(controllers = RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoleService roleService;
    @Autowired
    private ObjectMapper objectMapper;

    private RoleDto roleDto;

    @BeforeEach
    public void init() {
        roleDto = RoleDto.builder().id(1L).name("CLIENT").build();
    }

    @Test
    public void RoleController_AddRole_ReturnsCreated() throws Exception {
        given(roleService.addRole(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/role/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(roleDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void RoleController_GetAllRoles_ReturnResponseDto() throws Exception {
        List<RoleDto> roles = new ArrayList<>();
        roles.add(roleDto);

        when(roleService.findAllRoles()).thenReturn(roles);

        ResultActions response = mockMvc.perform(get("/role/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(roles.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void RoleController_GetRoleById_ReturnsRoleDto() throws Exception {
        when(roleService.findRoleById(1L)).thenReturn(roleDto);

        ResultActions response = mockMvc.perform(get("/role/find/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(roleDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void RoleController_UpdateRole_ReturnsRoleDto() throws Exception {
        when(roleService.updateRole(Mockito.any(RoleDto.class))).thenReturn(roleDto);

        ResultActions response = mockMvc.perform(put("/role/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(roleDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void UserController_DeleteUser_ReturnsResponse() throws Exception {
        doNothing().when(roleService).deleteRole(Mockito.any(Long.class));

        ResultActions response = mockMvc.perform(delete("/role/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
