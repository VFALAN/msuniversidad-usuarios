package com.vf.dev.msuniversidadusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableAsync
@Configuration
@ComponentScan(basePackages = "com.vf.dev.msuniversidadusuarios.*")
public class MsuniversidadUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsuniversidadUsuariosApplication.class, args);
	}

}
