package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.PerfilService;
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
@RequestMapping("/api/perfis")
@Tag(name = "Perfis", description = "Gerenciamento dos perfis de acesso do sistema (Roles)")
public class PerfilController {

    private final PerfilService service;

    PerfilController(PerfilService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Criar um novo perfil", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode criar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<PerfilDTO> criar(@Valid @RequestBody PerfilDTO dto) {
        // Chama o service para salvar o perfil e retorna o resultado
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode acessar esse endpoint
    @GetMapping
    @Operation(summary = "Listar todos os perfis", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode listar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<PerfilDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PreAuthorize("hasRole('ADMIN')")  // Apenas ADMIN pode atualizar perfil
    @PutMapping("/{id}") 
    @Operation(summary = "Atualizar perfil existente", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode atualizar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Perfil não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<PerfilDTO> atualizar( 
            @PathVariable Integer id,
            @Valid @RequestBody PerfilDTO dados) {

        PerfilDTO perfilAtualizado = service.atualizar(id, dados);
        return ResponseEntity.ok(perfilAtualizado);
    }
}