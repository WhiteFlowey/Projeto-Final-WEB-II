package br.com.gatekeeper.controle_acessos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ControleAcessosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleAcessosApplication.class, args);
	}

}
