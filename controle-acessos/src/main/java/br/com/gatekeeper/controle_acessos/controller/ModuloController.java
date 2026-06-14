package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.ModuloDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.ModuloService;
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
@RequestMapping("/api/modulos")
@Tag(name = "Módulos", description = "Gerenciamento dos módulos do sistema")
public class ModuloController {

    private final ModuloService service;

    ModuloController(ModuloService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Criar um novo módulo", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Módulo criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode criar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<ModuloDTO> criarModulo(@Valid @RequestBody ModuloDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarModulo(dto));
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping
    @Operation(summary = "Listar todos os módulos", description = "Requer perfil COMUM ou ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<ModuloDTO>> listarTodosModulos() {
        return ResponseEntity.ok(service.listarTodosModulos());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar módulo existente", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode atualizar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<ModuloDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody ModuloDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um módulo", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Módulo removido com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode deletar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado para remoção", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build(); 
    }

    @PreAuthorize("hasRole('COMUM')")
    @GetMapping("/{id}")
    @Operation(summary = "Buscar módulo por ID", description = "Requer perfil COMUM ou ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Módulo encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<ModuloDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }
}