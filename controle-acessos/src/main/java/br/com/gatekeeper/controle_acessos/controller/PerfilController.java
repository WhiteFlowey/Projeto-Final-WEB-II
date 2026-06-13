package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.service.PerfilService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; 
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    private final PerfilService service;

    PerfilController(PerfilService service) {
        this.service = service;
    }

 
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PerfilDTO> criar(@Valid @RequestBody PerfilDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PerfilDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}") 
    public ResponseEntity<PerfilDTO> atualizar( 
            @PathVariable Integer id,
            @RequestBody PerfilDTO dados) {


        PerfilDTO perfilAtualizado = service.atualizar(id, dados);

        return ResponseEntity.ok(perfilAtualizado);
    }
}