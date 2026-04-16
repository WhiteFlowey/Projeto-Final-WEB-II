package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.service.UsuarioService;
import jakarta.validation.Valid; // 1. IMPORTANTE: Adicione este import
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    // 2. O @Valid entra bem aqui, antes do @RequestBody
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.criarUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}