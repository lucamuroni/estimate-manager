package com.project.webapp.estimatemanager.service;

import com.project.webapp.estimatemanager.dtos.OptDto;
import com.project.webapp.estimatemanager.models.Opt;
import com.project.webapp.estimatemanager.repository.OptionRepo;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
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
public class OptionServiceTest {
    @Spy
    private ModelMapper modelMapper;
    @Mock
    private OptionRepo optionRepo;
    @InjectMocks
    private OptionService optionService;
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Opt dbOpt;
    private OptDto optDto;

    @BeforeEach
    public void init() {
        dbOpt = Opt.builder().id(1L).name("Sconto").type("2x1").build();
        optDto = OptDto.builder().id(1L).name("Sconto").type("2x1").build();
    }

    @Test
    public void OptionService_AddOption_ReturnsOptionDto() throws Exception {
        when(optionRepo.findOptByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(optionRepo.save(Mockito.any(Opt.class))).thenReturn(dbOpt);

        OptDto savedOpt = optionService.addOption(optDto);

        Assertions.assertThat(savedOpt).isNotNull();
    }

    @Test
    public void OptionService_FindAllOptions_ReturnsAllOptionsFromDb() throws Exception {
        List<Opt> opts = Mockito.mock(List.class);

        when(optionRepo.findAll()).thenReturn(opts);

        List<OptDto> optDtos = optionService.findAllOptions();

        Assertions.assertThat(optDtos).isNotNull();
    }

    @Test
    public void OptionService_FindOptionById_ReturnsOptionWithThatId() throws Exception {
        when(optionRepo.findOptById(Mockito.any(Long.class))).thenReturn(Optional.of(dbOpt));
        when(optionRepo.existsById(Mockito.any(Long.class))).thenReturn(true);

        OptDto optDto1 = optionService.findOptionById(1L);

        Assertions.assertThat(optDto1).isNotNull();
    }

    @Test
    public void OptionService_UpdateOption_ReturnsUpdatedOption() throws Exception {
        optDto.setName("Sconticino");
        Opt modifiedOpt = Opt.builder().id(1L).name("Sconticino").type("2x1").build();

        when(optionRepo.findOptById(Mockito.any(Long.class))).thenReturn(Optional.of(dbOpt));
        when(optionRepo.findOptByName(Mockito.any(String.class))).thenReturn(Optional.empty());
        when(optionRepo.save(Mockito.any(Opt.class))).thenReturn(modifiedOpt);

        OptDto savedOpt = optionService.updateOption(optDto);

        Assertions.assertThat(savedOpt).isNotNull();
    }

    @Test
    public void OptionService_DeleteOption_ReturnsEmptyOption() {
        when(optionRepo.findOptById(Mockito.any(Long.class))).thenReturn(Optional.of(dbOpt));

        assertAll(() -> optionService.deleteOption(1L));
    }
}
