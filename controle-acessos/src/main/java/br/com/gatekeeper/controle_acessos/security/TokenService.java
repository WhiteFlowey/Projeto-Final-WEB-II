package br.com.gatekeeper.controle_acessos.security;

import br.com.gatekeeper.controle_acessos.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Essa anotação puxa a "senha secreta" da nossa API lá do application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            // Define o algoritmo e a senha secreta que vai "assinar" o token
            var algoritmo = Algorithm.HMAC256(secret);
            
            return JWT.create()
                    .withIssuer("API Gatekeeper") // Quem está emitindo o token
                    .withSubject(usuario.getEmail()) // Quem é o dono do token (vamos usar o e-mail)
                    .withExpiresAt(dataExpiracao()) // Quando o token vence
                    .sign(algoritmo); // Assina e finaliza
                    
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        // O token vai valer por 2 horas a partir do momento do login, no fuso horário de Brasília
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer("API Gatekeeper")
                    .build()
                    .verify(tokenJWT)
                    .getSubject(); // Puxa o e-mail que guardamos no token
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }
}