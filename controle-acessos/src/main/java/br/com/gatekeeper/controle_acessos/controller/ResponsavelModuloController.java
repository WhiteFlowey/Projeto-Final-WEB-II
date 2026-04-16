package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import br.com.gatekeeper.controle_acessos.service.ResponsavelModuloService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelModuloController {

    @Autowired
    private ResponsavelModuloService service;

    // Repare que agora o ResponseEntity devolve o ResponsavelModuloDTO
    @PostMapping
    public ResponseEntity<ResponsavelModuloDTO> definirResponsavel(@Valid @RequestBody ResponsavelModulo responsavel) {
        ResponsavelModuloDTO novoResponsavel = service.definirResponsavel(responsavel);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoResponsavel);
    }

    // A lista também devolve os DTOs
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<ResponsavelModuloDTO>> listarPorModulo(@PathVariable Integer moduloId) {
        return ResponseEntity.ok(service.listarPorModulo(moduloId));
    }
}