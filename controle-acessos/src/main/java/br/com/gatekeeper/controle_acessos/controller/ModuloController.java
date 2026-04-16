package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.ModuloDTO;
import br.com.gatekeeper.controle_acessos.service.ModuloService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    @Autowired
    private ModuloService service;

    @PostMapping
    public ResponseEntity<ModuloDTO> salvar(@Valid @RequestBody ModuloDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<ModuloDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}