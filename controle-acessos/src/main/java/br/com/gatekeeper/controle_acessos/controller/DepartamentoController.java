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

    // 3. Atualiza um departamento existente
    @PutMapping("/{id}")
    public ResponseEntity<DepartamentoDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody DepartamentoDTO dto) {
        // Confirme se o nome do método no seu Service é 'atualizar' mesmo!
        DepartamentoDTO atualizado = service.atualizar(id, dto); 
        return ResponseEntity.ok(atualizado);
    }

    // 4. Deleta um departamento pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        // Confirme se o nome do método no seu Service é 'deletar' ou 'excluir'!
        service.deletar(id); 
        return ResponseEntity.noContent().build(); // Retorna 204 No Content (Sucesso, sem corpo)
    }
}