package com.project.webapp.estimatemanager;

import com.project.webapp.estimatemanager.dtos.EstimateDto;
import com.project.webapp.estimatemanager.models.Estimate;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.spi.MappingContext;
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

    PropertyMap<Estimate, EstimateDto> estimateMapping = new PropertyMap<Estimate, EstimateDto>() {
        @Override
        protected void configure() {
            map().setClientEmail(source.getClient().getEmail());
            map().setEmployeeEmail(source.getEmployee().getEmail());
            map().setProductName(source.getProduct().getName());
        }
    };

    Converter<String, String> converter = new Converter<String, String>() {
        @Override
        public String convert(MappingContext<String, String> mappingContext) {
            return mappingContext.getSource() == null ? "" : mappingContext.getSource().trim();
        }
    };
}
