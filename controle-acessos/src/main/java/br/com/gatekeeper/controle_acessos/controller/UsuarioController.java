package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.criarUsuario(request);
        adicionarLinks(response); // Injeta os links antes de devolver
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UsuarioResponseDTO>> listarTodos(
            @ParameterObject @PageableDefault(page = 0, size = 10) Pageable paginacao) {
            
        Page<UsuarioResponseDTO> pagina = usuarioService.listarTodos(paginacao);
        
        pagina.forEach(this::adicionarLinks);
        
        return ResponseEntity.ok(pagina);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        UsuarioResponseDTO response = usuarioService.buscarPorId(id);
        adicionarLinks(response); // Injeta os links antes de devolver
        return ResponseEntity.ok(response);
    }

    // Atualizar
    @PreAuthorize("hasRole('COMUM')")
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.atualizarUsuario(id, request);
        adicionarLinks(response); // Injeta os links antes de devolver
        return ResponseEntity.ok(response);
    }

    // Remover
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id) {
        usuarioService.removerUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // MÉTODO AUXILIAR PARA MONTAR O HATEOAS
    private void adicionarLinks(UsuarioResponseDTO dto) {
        Integer id = dto.getId();
        
        // 1. Link para si mesmo (GET /api/usuarios/{id})
        dto.add(linkTo(methodOn(UsuarioController.class).buscarPorId(id)).withSelfRel());
        
        // 2. Link indicando como atualizar (PUT /api/usuarios/{id})
        dto.add(linkTo(methodOn(UsuarioController.class).atualizar(id, null)).withRel("atualizar"));
        
        // 3. Link indicando como deletar (DELETE /api/usuarios/{id})
        dto.add(linkTo(methodOn(UsuarioController.class).remover(id)).withRel("deletar"));
        
        // 4. Link indicando como voltar para a lista completa (GET /api/usuarios)
        dto.add(linkTo(methodOn(UsuarioController.class).listarTodos(null)).withRel("listar_todos"));
    }
}