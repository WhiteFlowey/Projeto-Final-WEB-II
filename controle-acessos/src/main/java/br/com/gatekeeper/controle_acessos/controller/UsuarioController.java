package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> criarUsuario(@Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.criarUsuario(request);
        adicionarLinks(response); // Injeta os links antes de devolver
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        List<UsuarioResponseDTO> lista = usuarioService.listarTodos();
        // Percorre a lista e injeta os links em cada usuário individualmente
        lista.forEach(this::adicionarLinks);
        return ResponseEntity.ok(lista);
    }

    // Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@PathVariable Integer id) {
        UsuarioResponseDTO response = usuarioService.buscarPorId(id);
        adicionarLinks(response); // Injeta os links antes de devolver
        return ResponseEntity.ok(response);
    }

    // Atualizar
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizar(@PathVariable Integer id, @Valid @RequestBody UsuarioRequestDTO request) {
        UsuarioResponseDTO response = usuarioService.atualizarUsuario(id, request);
        adicionarLinks(response); // Injeta os links antes de devolver
        return ResponseEntity.ok(response);
    }

    // Remover
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
        // Passamos 'null' no request apenas para o methodOn conseguir montar a URL correta
        dto.add(linkTo(methodOn(UsuarioController.class).atualizar(id, null)).withRel("atualizar"));
        
        // 3. Link indicando como deletar (DELETE /api/usuarios/{id})
        dto.add(linkTo(methodOn(UsuarioController.class).remover(id)).withRel("deletar"));
        
        // 4. Link indicando como voltar para a lista completa (GET /api/usuarios)
        dto.add(linkTo(methodOn(UsuarioController.class).listarTodos()).withRel("listar_todos"));
    }
}