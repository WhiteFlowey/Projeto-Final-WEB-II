package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.FaqRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.FaqResponseDTO;
import br.com.gatekeeper.controle_acessos.service.FaqService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
public class FaqController {

    @Autowired
    private FaqService service;

    // 1. Mudamos para receber FaqRequestDTO e devolver FaqResponseDTO
    @PostMapping("/modulo/{moduloId}")
    public ResponseEntity<FaqResponseDTO> criarFaq(@Valid @RequestBody FaqRequestDTO dto, @PathVariable Integer moduloId) {
        FaqResponseDTO novoFaq = service.criarFaq(dto, moduloId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFaq);
    }

    // 2. Mudamos para devolver uma lista de FaqResponseDTO
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<FaqResponseDTO>> listarPorModulo(@PathVariable Integer moduloId) {
        return ResponseEntity.ok(service.listarPorModulo(moduloId));
    }
}