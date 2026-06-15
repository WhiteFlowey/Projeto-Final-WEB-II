package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.dto.request.ResponsavelModuloRequestDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.ResponsavelModuloService;
import jakarta.validation.Valid;

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

import java.util.List;

@RestController
@RequestMapping("/api/responsaveis")
@Tag(name = "Responsáveis por Módulo", description = "Gerenciamento de quais usuários são diretores/responsáveis por cada módulo")
public class ResponsavelModuloController {

    private final ResponsavelModuloService service;

    ResponsavelModuloController(ResponsavelModuloService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Atribuir um novo responsável a um módulo", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Responsável atribuído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode atribuir)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário ou Módulo não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<ResponsavelModuloDTO> criar(@Valid @RequestBody ResponsavelModuloRequestDTO dto) {
        // Ajustado para 201 CREATED
        return ResponseEntity.status(HttpStatus.CREATED).body(service.atribuirNovoDiretor(dto));
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping
    @Operation(summary = "Listar todos os responsáveis", description = "Requer perfil COMUM ou ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<ResponsavelModuloDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/buscar")
    @Operation(summary = "Buscar responsáveis pelo nome do usuário", description = "Requer perfil COMUM ou ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<ResponsavelModuloDTO>> buscar(@RequestParam String nome) {
        return ResponseEntity.ok(service.listarPorNome(nome));
    }
}