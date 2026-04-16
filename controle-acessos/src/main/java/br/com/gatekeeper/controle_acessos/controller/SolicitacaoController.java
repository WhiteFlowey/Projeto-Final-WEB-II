package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.service.SolicitacaoService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitacoes")
public class SolicitacaoController {

    @Autowired
    private SolicitacaoService solicitacaoService;

    // Responde a requisições POST em /api/solicitacoes
    @PostMapping
    public ResponseEntity<SolicitacaoResponseDTO> criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO request) {
        
        // O Service vai validar os dados, gerar o protocolo, a data, e salvar no banco
        SolicitacaoResponseDTO response = solicitacaoService.criarSolicitacao(request);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}