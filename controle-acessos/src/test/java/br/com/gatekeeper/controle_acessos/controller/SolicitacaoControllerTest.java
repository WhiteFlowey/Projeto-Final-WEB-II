package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.security.TokenService;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import br.com.gatekeeper.controle_acessos.service.SolicitacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = SolicitacaoController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita a barreira principal do Spring Security
class SolicitacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SolicitacaoService solicitacaoService;

    // ====================================================================
    // MOCKS OBRIGATÓRIOS (Para o contexto do Spring carregar sem erros)
    // ====================================================================
    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private CacheManager cacheManager; // Resolve o erro do @EnableCaching

    // ====================================================================

    @Test
    @WithMockUser(roles = "COMUM") // Simula um usuário logado com a role exigida pelo @PreAuthorize
    void deveRetornarStatus201QuandoSolicitacaoForValida() throws Exception {
        // ARRANGE
        SolicitacaoRequestDTO request = new SolicitacaoRequestDTO();
        request.setUsuarioId(1);
        request.setModuloId(10);
        request.setJustificativa("Trabalho");
        request.setQtdDias(5); // Valor válido (> 0)

        SolicitacaoResponseDTO response = new SolicitacaoResponseDTO();
        response.setId(100);

        when(solicitacaoService.criarSolicitacao(any(SolicitacaoRequestDTO.class))).thenReturn(response);

        // ACT & ASSERT
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    @WithMockUser(roles = "COMUM") // Simula o usuário logado também no cenário de erro
    void deveRetornarStatus400QuandoQtdDiasForInvalida() throws Exception {
        // ARRANGE
        SolicitacaoRequestDTO request = new SolicitacaoRequestDTO();
        request.setUsuarioId(1);
        request.setModuloId(10);
        request.setJustificativa("Trabalho");
        request.setQtdDias(0); // INVÁLIDO (Forçando o erro do @Valid)

        // ACT & ASSERT
        mockMvc.perform(post("/api/solicitacoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}