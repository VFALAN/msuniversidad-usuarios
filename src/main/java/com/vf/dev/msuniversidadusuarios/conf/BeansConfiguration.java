package com.vf.dev.msuniversidadusuarios.conf;

import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@Slf4j
@EnableAsync
public class BeansConfiguration {
    @Value("${msuniversidad.version}")
    private String version;

    @Bean
    public ModelMapper modelMapper() {
        log.info("MsUniversidad Version: " + this.version);
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public SpringFormEncoder feingFormEncoder() {
        return new SpringFormEncoder();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
