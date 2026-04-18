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
    public ResponseEntity<ModuloDTO> criarModulo(@Valid @RequestBody ModuloDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarModulo(dto));
    }

    @GetMapping
    public ResponseEntity<List<ModuloDTO>> listarTodosModulos() {
        return ResponseEntity.ok(service.listarTodosModulos());
    }

    // 3. ATUALIZAR (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<ModuloDTO> atualizar(@PathVariable Integer id, @RequestBody ModuloDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // 4. DELETAR (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        // Retorna 204 No Content (Sucesso, mas sem corpo na resposta)
        return ResponseEntity.noContent().build(); 
    }

    // 5. BUSCAR POR ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}