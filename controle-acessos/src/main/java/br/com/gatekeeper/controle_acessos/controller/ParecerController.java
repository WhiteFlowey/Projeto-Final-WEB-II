package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.service.ParecerService;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pareceres")
public class ParecerController {

    @Autowired
    private ParecerService parecerService;

    // Recebe a decisão do Gestor (APROVADA/REJEITADA) e processa tudo
    @PostMapping
    public ResponseEntity<ParecerResponseDTO> avaliarSolicitacao(@Valid @RequestBody ParecerRequestDTO request) {
        ParecerResponseDTO response = parecerService.avaliarSolicitacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ParecerResponseDTO>> listarTodos() {
        return ResponseEntity.ok(parecerService.listarTodos());
}
}