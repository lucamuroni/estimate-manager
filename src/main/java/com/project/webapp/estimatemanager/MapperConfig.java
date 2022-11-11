package com.project.webapp.estimatemanager;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.models.Estimate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        modelMapper.addMappings(estimateMapping);
        modelMapper.addConverter(converter);
        return modelMapper;
    }

    PropertyMap<Estimate, EstimateDto> estimateMapping = new PropertyMap<>() {
        @Override
        protected void configure() {
            map().setPrice(source.getPrice());
        }
    };

    Converter<String, String> converter = mappingContext -> mappingContext.getSource() == null ? "" : mappingContext.getSource().trim();
}
