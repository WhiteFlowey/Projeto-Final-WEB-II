package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.service.UsuarioService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Indica que esta classe vai responder a pedidos HTTP devolvendo JSON
@RequestMapping("/api/usuarios") // O endereço base no navegador/Postman
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Quando chegar um POST no endereço /api/usuarios, este método entra em ação
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@RequestBody UsuarioRequestDTO request) {
        
        // Passa o DTO para o Service processar e salvar
        UsuarioResponseDTO response = usuarioService.criarUsuario(request);
        
        // Devolve o DTO de resposta com o status HTTP 201 (Created - Criado com sucesso)
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}