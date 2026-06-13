package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.ModuloDTO;
import br.com.gatekeeper.controle_acessos.service.ModuloService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    private final ModuloService service;

    ModuloController(ModuloService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ModuloDTO> criarModulo(@Valid @RequestBody ModuloDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarModulo(dto));
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping
    public ResponseEntity<List<ModuloDTO>> listarTodosModulos() {
        return ResponseEntity.ok(service.listarTodosModulos());
    }

    // 3. ATUALIZAR (PUT)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ModuloDTO> atualizar(@PathVariable Integer id, @RequestBody ModuloDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // 4. DELETAR (DELETE)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        // Retorna 204 No Content (Sucesso, mas sem corpo na resposta)
        return ResponseEntity.noContent().build(); 
    }

    // 5. BUSCAR POR ID (GET)
    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/{id}")
    public ResponseEntity<ModuloDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}