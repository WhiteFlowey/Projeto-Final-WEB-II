package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.DepartamentoDTO; // Importar o DTO
import br.com.gatekeeper.controle_acessos.service.DepartamentoService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService service;

    DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    // Recebe DepartamentoDTO e retorna DepartamentoDTO
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<DepartamentoDTO> criar(@Valid @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    // Retorna uma lista de DepartamentoDTO
    @PreAuthorize("hasRole('COMUM')")
    @GetMapping
    public ResponseEntity<List<DepartamentoDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // Atualiza um departamento existente
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody DepartamentoDTO dto) {
        DepartamentoDTO atualizado = service.atualizar(id, dto); 
        return ResponseEntity.ok(atualizado);
    }

    // Deleta um departamento pelo ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id); 
        return ResponseEntity.noContent().build(); // Retorna 204 No Content (Sucesso, sem corpo)
    }
}