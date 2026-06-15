package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.DepartamentoDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.DepartamentoService;
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

@RestController // Indica que esta classe é um Controller REST
@RequestMapping("/api/departamentos")  // URL base dos endpoints
@Tag(name = "Departamentos", description = "Gerenciamento dos departamentos da empresa")
public class DepartamentoController {

    // Service responsável pelas regras de negócio
    private final DepartamentoService service;

    DepartamentoController(DepartamentoService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode criar
    @PostMapping // Endpoint POST
    @Operation(summary = "Criar um novo departamento", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Departamento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode criar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<DepartamentoDTO> criar(@Valid @RequestBody DepartamentoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }
    // Usuário COMUM pode listar, mas não criar, atualizar ou deletar departamentos
    @PreAuthorize("hasRole('COMUM')")
    @GetMapping
    @Operation(summary = "Listar todos os departamentos", description = "Requer perfil COMUM ou ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<DepartamentoDTO>> listarTodos() {
        // Retorna todos os departamentos
        return ResponseEntity.ok(service.listarTodos());
    }

    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode atualizar
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um departamento existente", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Departamento atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode atualizar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Departamento não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<DepartamentoDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody DepartamentoDTO dto) {
        DepartamentoDTO atualizado = service.atualizar(id, dto); 
        return ResponseEntity.ok(atualizado);
    }

    @PreAuthorize("hasRole('ADMIN')") // Apenas ADMIN pode excluir
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um departamento", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Departamento removido com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode deletar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Departamento não encontrado para remoção", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id); 
        return ResponseEntity.noContent().build();
    }
}