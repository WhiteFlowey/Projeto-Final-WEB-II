package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.service.PerfilService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    @Autowired 
    private PerfilService service;

    @PostMapping
    public ResponseEntity<PerfilDTO> criar(@Valid @RequestBody PerfilDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<PerfilDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}