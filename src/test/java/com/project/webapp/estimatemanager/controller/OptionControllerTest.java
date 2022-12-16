package com.project.webapp.estimatemanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.service.OptionService;
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

@WebMvcTest(controllers = OptionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OptionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OptionService optionService;
    @Autowired
    private ObjectMapper objectMapper;

    private OptDto optDto;

    @BeforeEach
    public void init() {
        optDto = OptDto.builder().id(1L).name("Sconto").type("2x1").build();
    }

    @Test
    public void OptionController_AddOption_ReturnsCreated() throws Exception {
        given(optionService.addOption(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/option/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optDto)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(optDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void OptionController_GetAllOptions_ReturnResponseDto() throws Exception {
        List<OptDto> options = new ArrayList<>();
        options.add(optDto);

        when(optionService.findAllOptions()).thenReturn(options);

        ResultActions response = mockMvc.perform(get("/option/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(options.size())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void OptionController_GetOptionById_ReturnsOptDto() throws Exception {
        when(optionService.findOptionById(1L)).thenReturn(optDto);

        ResultActions response = mockMvc.perform(get("/option/find/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(optDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void OptionController_UpdateOption_ReturnsOptDto() throws Exception {
        when(optionService.updateOption(Mockito.any(OptDto.class))).thenReturn(optDto);

        ResultActions response = mockMvc.perform(put("/option/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(optDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(optDto.getName())))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void OptionController_DeleteOption_ReturnsResponse() throws Exception {
        doNothing().when(optionService).deleteOption(Mockito.any(Long.class));

        ResultActions response = mockMvc.perform(delete("/option/delete/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }
}
