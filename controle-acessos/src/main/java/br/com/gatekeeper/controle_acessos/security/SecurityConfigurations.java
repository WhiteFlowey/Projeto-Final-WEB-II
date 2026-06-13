package br.com.gatekeeper.controle_acessos.security;

import jakarta.servlet.http.HttpServletResponse; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Garante que o @PreAuthorize vai funcionar nos Controllers
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter; 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> {
                    // Mantemos as portas abertas apenas para login e documentação
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll(); 
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();
                    
                    // Qualquer outra rota exige Token válido
                    req.anyRequest().authenticated(); 
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) 
                
                .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json; charset=UTF-8");
                        response.getWriter().write("""
                                {
                                    "status": 403,
                                    "erro": "Acesso Negado",
                                    "mensagem": "Token JWT ausente, inválido ou expirado. Faça login para acessar este recurso.",
                                    "caminho": "%s"
                                }
                                """.formatted(request.getRequestURI()));
                    })
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy(
            "ROLE_ADMIN > ROLE_GESTOR \n" +
            "ROLE_GESTOR > ROLE_COMUM"
        );
    }
}