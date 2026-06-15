package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.response.NotificacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.NotificacaoService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
@Tag(name = "Notificações", description = "Visualização das notificações enviadas aos usuários")
public class NotificacaoController {

    private final NotificacaoService service;

    NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar notificações de um usuário específico", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de notificações recuperada com sucesso (pode retornar lista vazia)"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<NotificacaoResponseDTO>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarNotificacoesDoUsuario(usuarioId));
    }
}