package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.dto.request.ResponsavelModuloRequestDTO;
import br.com.gatekeeper.controle_acessos.service.ResponsavelModuloService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelModuloController {

    private final ResponsavelModuloService service;

    ResponsavelModuloController(ResponsavelModuloService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ResponsavelModuloDTO> criar(@RequestBody ResponsavelModuloRequestDTO dto) {
        return ResponseEntity.ok(service.atribuirNovoDiretor(dto));
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping
    public ResponseEntity<List<ResponsavelModuloDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/buscar")
    public ResponseEntity<List<ResponsavelModuloDTO>> buscar(@RequestParam String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }
}