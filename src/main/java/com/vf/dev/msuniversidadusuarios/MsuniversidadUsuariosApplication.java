package com.vf.dev.msuniversidadusuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@ComponentScan(basePackages = "com.vf.dev.msuniversidadusuarios.*")
public class MsuniversidadUsuariosApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsuniversidadUsuariosApplication.class, args);
	}

}
