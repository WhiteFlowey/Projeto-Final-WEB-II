package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.SolicitacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
@RequestMapping("/api/solicitacoes")
@RequiredArgsConstructor 
@Tag(name = "Solicitações", description = "Gerenciamento das solicitações de acesso aos módulos do sistema")
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    @PreAuthorize("hasRole('COMUM')")
    @PostMapping
    @Operation(summary = "Criar uma nova solicitação de acesso", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas usuários com perfil COMUM podem solicitar acesso)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou Módulo não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<SolicitacaoResponseDTO> criarSolicitacao(@Valid @RequestBody SolicitacaoRequestDTO request) {
        SolicitacaoResponseDTO response = solicitacaoService.criarSolicitacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @GetMapping
    @Operation(summary = "Listar todas as solicitações do sistema", description = "Requer perfil de GESTOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitações recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas GESTOR pode listar todas as solicitações)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(solicitacaoService.listarTodas());
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar solicitações de um usuário específico", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de solicitações do usuário recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<SolicitacaoResponseDTO>> listarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(solicitacaoService.listarPorUsuario(usuarioId));
    }
}