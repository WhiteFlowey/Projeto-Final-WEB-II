package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.HistoricoAcessoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.service.HistoricoAcessoService;
import org.springdoc.core.annotations.ParameterObject; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; 
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoAcessoController {

    private final HistoricoAcessoService service;

    HistoricoAcessoController(HistoricoAcessoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<HistoricoAcessoResponseDTO>> listarTodoHistorico(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable paginacao) { 
            // 👆 ANOTAÇÃO INSERIDA AQUI
        return ResponseEntity.ok(service.listarHistoricoPaginado(paginacao));
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Page<HistoricoAcessoResponseDTO>> buscarHistoricoDoUsuario(
            @PathVariable Integer usuarioId, 
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable paginacao) {
            // 👆 ANOTAÇÃO INSERIDA AQUI TAMBÉM
        return ResponseEntity.ok(service.buscarHistoricoDoUsuario(usuarioId, paginacao));
    }

    // POST /api/historicos
    @PreAuthorize("hasRole('COMUM')")
    @PostMapping
    public ResponseEntity<HistoricoAcessoResponseDTO> registrarAcesso(@RequestBody HistoricoAcessoRequestDTO dto) {
        HistoricoAcessoResponseDTO registrado = service.registrarAcesso(dto);
        return ResponseEntity.status(201).body(registrado);
    }
}