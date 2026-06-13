package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor; // Importação do Lombok

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor // O Lombok injeta o Service automaticamente aqui
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PreAuthorize("hasRole('COMUM')")
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO request) {
        
        // O Service vai validar os dados, gerar o protocolo, a data, e salvar no banco
        SolicitacaoResponseDTO response = solicitacaoService.criarSolicitacao(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(solicitacaoService.listarTodas());
    }

    // Rota para listar solicitações de UM usuário específico
    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(solicitacaoService.listarPorUsuario(usuarioId));
    }
}