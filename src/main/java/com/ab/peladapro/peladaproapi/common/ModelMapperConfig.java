package com.ab.peladapro.peladaproapi.common;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper strictModelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.addConverter((MappingContext<String, UUID> ctx) -> {
            String value = ctx.getSource();
            return value == null || value.isBlank() ? null : UUID.fromString(value);
        });
        Converter<Long, UUID> longToUuid = ctx -> null;
        mapper.addConverter(longToUuid, Long.class, UUID.class);
        mapper.addConverter((MappingContext<UUID, String> ctx) -> {
            UUID value = ctx.getSource();
            return value == null ? null : value.toString();
        });
        return mapper;
    }
}
