package com.roomify.detection_be.config;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy.Provider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class AppConfig {
  @Bean
  ObjectMapper objectMapper() {
    ObjectMapper objectMapper =
        JsonMapper.builder()
            .accessorNaming(new Provider().withBuilderPrefix(""))
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    objectMapper.registerModules(new JavaTimeModule());

    return objectMapper;
  }

  @Bean
  MappingJackson2HttpMessageConverter jackson2HttpMessageConverter(
      final ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
  }
}
