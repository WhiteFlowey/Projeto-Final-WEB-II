package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.FaqRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.FaqResponseDTO;
import br.com.gatekeeper.controle_acessos.exception.ErroRespostaDTO;
import br.com.gatekeeper.controle_acessos.service.FaqService;
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
@RequestMapping("/api/faqs")
@Tag(name = "FAQs", description = "Gerenciamento das Perguntas Frequentes (FAQ) dos módulos")
public class FaqController {

    private final FaqService service;

    FaqController(FaqService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('GESTOR')") // Apenas GESTOR pode criar FAQ
    @PostMapping("/modulo/{moduloId}")
    @Operation(summary = "Criar um novo FAQ para um módulo", description = "Requer perfil de GESTOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "FAQ criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas GESTOR pode criar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "Módulo não encontrado na base de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<FaqResponseDTO> criarFaq(@Valid @RequestBody FaqRequestDTO dto, @PathVariable Integer moduloId) {
        // Chama o service para criar o FAQ e retorna o resultado
        FaqResponseDTO novoFaq = service.criarFaq(dto, moduloId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFaq);
    }

    @PreAuthorize("hasRole('COMUM')") // Usuário COMUM pode consultar
    @GetMapping("/modulo/{moduloId}")
    @Operation(summary = "Listar todos os FAQs de um módulo", description = "Requer perfil COMUM.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de FAQs recuperada com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<List<FaqResponseDTO>> listarPorModulo(@PathVariable Integer moduloId) {
        // Retorna todos os FAQs de um módulo
        return ResponseEntity.ok(service.listarPorModulo(moduloId));
    }

    @PreAuthorize("hasRole('GESTOR')")
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um FAQ existente", description = "Requer perfil de GESTOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "FAQ atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas GESTOR pode atualizar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "FAQ não encontrado na base de dados", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<FaqResponseDTO> atualizarFaq(@PathVariable Integer id, @Valid @RequestBody FaqRequestDTO dto) {
        // Atualiza FAQ existente
        FaqResponseDTO atualizado = service.atualizarFaq(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @PreAuthorize("hasRole('GESTOR')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar um FAQ", description = "Requer perfil de GESTOR.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "FAQ removido com sucesso"),
        @ApiResponse(responseCode = "403", description = "Acesso Negado (Apenas GESTOR pode deletar)", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class))),
        @ApiResponse(responseCode = "404", description = "FAQ não encontrado para remoção", 
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErroRespostaDTO.class)))
    })
    public ResponseEntity<Void> deletarFaq(@PathVariable Integer id) {
        service.deletarFaq(id);
        return ResponseEntity.noContent().build();
    }
}