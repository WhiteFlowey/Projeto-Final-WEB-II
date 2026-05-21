package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.HistoricoAcessoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.service.HistoricoAcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; 
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoAcessoController {

    @Autowired private HistoricoAcessoService service;

    @GetMapping
    public ResponseEntity<Page<HistoricoAcessoResponseDTO>> listarTodoHistorico(
            @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable paginacao) {
        return ResponseEntity.ok(service.listarHistoricoPaginado(paginacao));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<HistoricoAcessoResponseDTO>> buscarHistoricoDoUsuario(
            @PathVariable Integer usuarioId, 
            @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable paginacao) {
        return ResponseEntity.ok(service.buscarHistoricoDoUsuario(usuarioId, paginacao));
    }

    // POST /api/historicos
    @PostMapping
    public ResponseEntity<HistoricoAcessoResponseDTO> registrarAcesso(@RequestBody HistoricoAcessoRequestDTO dto) {
        HistoricoAcessoResponseDTO registrado = service.registrarAcesso(dto);
        return ResponseEntity.status(201).body(registrado);
    }
}