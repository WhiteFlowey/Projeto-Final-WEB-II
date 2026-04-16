package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.DepartamentoDTO; // Importar o DTO
import br.com.gatekeeper.controle_acessos.service.DepartamentoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired 
    private DepartamentoService service;

    // 1. Recebe DepartamentoDTO e retorna DepartamentoDTO
    @PostMapping
    public ResponseEntity<DepartamentoDTO> criar(@Valid @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    // 2. Retorna uma lista de DepartamentoDTO
    @GetMapping
    public ResponseEntity<List<DepartamentoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}