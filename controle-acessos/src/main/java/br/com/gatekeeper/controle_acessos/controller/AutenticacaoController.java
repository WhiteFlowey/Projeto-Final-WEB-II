package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.DadosAutenticacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody DadosAutenticacao dados) {
        // 1. Pega o email e senha que vieram do Front-end (Flutter/Postman)
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        
        // 2. Chama o Spring Security para validar (ele vai lá no nosso AutenticacaoService e checa o hash no banco)
        var authentication = manager.authenticate(authenticationToken);

        // 3. Se chegou aqui, a senha está correta e o usuário está ATIVO!
        // Por enquanto, vamos devolver um OK (Código 200). 
        return ResponseEntity.ok("Login realizado com sucesso! Usuário autenticado.");
    }
}