package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.HistoricoAcessoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.HistoricoAcessoService;
import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject; 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; 
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/historicos")
@Tag(name = "Histórico de Acessos", description = "Auditoria e gerenciamento dos registros de acesso aos módulos")
public class HistoricoAcessoController {

    private final HistoricoAcessoService service;

    HistoricoAcessoController(HistoricoAcessoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Listar todo o histórico de acessos (Paginado)", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico recuperado com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode listar todo o histórico)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<Page<HistoricoAcessoResponseDTO>> listarTodoHistorico(
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable paginacao) { 
        return ResponseEntity.ok(service.listarHistoricoPaginado(paginacao));
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Buscar histórico de acessos de um usuário (Paginado)", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico do usuário recuperado com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<Page<HistoricoAcessoResponseDTO>> buscarHistoricoDoUsuario(
            @PathVariable Integer usuarioId, 
            @ParameterObject @PageableDefault(page = 0, size = 10, sort = "dataInicio", direction = Sort.Direction.DESC) Pageable paginacao) {
        return ResponseEntity.ok(service.buscarHistoricoDoUsuario(usuarioId, paginacao));
    }

    @PreAuthorize("hasRole('COMUM')")
    @PostMapping
    @Operation(summary = "Registrar um novo acesso no histórico", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Acesso registrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou Módulo não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<HistoricoAcessoResponseDTO> registrarAcesso(@Valid @RequestBody HistoricoAcessoRequestDTO dto) {
        HistoricoAcessoResponseDTO registrado = service.registrarAcesso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrado);
    }
}