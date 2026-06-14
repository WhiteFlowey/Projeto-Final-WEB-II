package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.UsuarioService;
import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Criar um novo usuário", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode criar usuários)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.criarUsuario(request);
        adicionarLinks(response);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> lista = usuarioService.listarTodos();
        // Percorre a lista e injeta os links em cada usuário individualmente
        lista.forEach(this::adicionarLinks);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Usuários comuns só podem buscar o próprio ID. ADMINs podem buscar qualquer um.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Tentativa de ver perfil de outro usuário)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado no banco de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        UsuarioResponseDTO response = usuarioService.buscarPorId(id);
        adicionarLinks(response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('COMUM')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.atualizarUsuario(id, request);
        adicionarLinks(response);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário", description = "Requer perfil de ADMIN.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas ADMIN pode remover)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Usuário não encontrado para remoção", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private void adicionarLinks(UsuarioResponseDTO dto) {
        Integer id = dto.getId();
        
        dto.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        
        // 2. Link indicando como atualizar (PUT /api/usuarios/{id})
        // Passamos 'null' no request apenas para o methodOn conseguir montar a URL correta
        dto.add(linkTo(methodOn(UsuarioController.class).atualizar(id, null)).withRel("atualizar"));
        dto.add(linkTo(methodOn(UsuarioController.class).remover(id)).withRel("deletar"));
        
        // 4. Link indicando como voltar para a lista completa (GET /api/usuarios)
        dto.add(linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("listar_todos"));
    }
}