package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.ParecerService;
import jakarta.validation.Valid;

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
@RequestMapping("/api/pareceres")
@Tag(name = "Pareceres", description = "Gerenciamento das avaliações (pareceres) de solicitações de acesso")
public class ParecerController {

    private final ParecerService parecerService;

    ParecerController(ParecerService parecerService) {
        this.parecerService = parecerService;
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PostMapping
    @Operation(summary = "Avaliar uma solicitação de acesso", description = "Requer perfil de GESTOR. Recebe a decisão (APROVADA/REJEITADA) e processa o histórico e notificações.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Parecer registrado e processado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas GESTOR pode avaliar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Solicitação ou Avaliador não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<ParecerResponseDTO> avaliarSolicitacao(@Valid @RequestBody ParecerRequestDTO request) {
        ParecerResponseDTO response = parecerService.avaliarSolicitacao(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Listar todos os pareceres", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pareceres recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode listar todos)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<ParecerResponseDTO>> listarTodos() {
        return ResponseEntity.ok(parecerService.listarTodos());
    }
}