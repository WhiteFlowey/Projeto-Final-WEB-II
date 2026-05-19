package br.com.gatekeeper.controle_acessos.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                // 1. Configura o botão de "Authorize" (O Cadeado)
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // 2. Exige esse cadeado em todas as rotas da API
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                // 3. Deixa a página bonita com o título do seu projeto
                .info(new Info()
                        .title("Gatekeeper API")
                        .description("API RESTful para Gestão e Controle de Acessos")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("admin@gatekeeper.com")));
    }
}