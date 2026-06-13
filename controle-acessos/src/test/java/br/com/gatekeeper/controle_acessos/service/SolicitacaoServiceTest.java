package br.com.gatekeeper.controle_acessos.service;

import java.util.List;
import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.SolicitacaoMapper;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.Solicitacao;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.SolicitacaoRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Essencial: Ativa o Mockito nesta classe
class SolicitacaoServiceTest {

    // Cria "dublês" (Mocks) para não precisarmos do banco de dados real
    @Mock private SolicitacaoRepository solicitacaoRepository;
    @Mock private UsuarioRepository usuarioRepository;
    @Mock private ModuloRepository moduloRepository;
    @Mock private SolicitacaoMapper solicitacaoMapper;

    // Injeta os dublês dentro do nosso Service real
    @InjectMocks private SolicitacaoService solicitacaoService;

    @Test
    void deveCriarSolicitacaoComSucesso() {
        // ==========================================
        // ARRANGE (Preparar)
        // ==========================================
        SolicitacaoRequestDTO request = new SolicitacaoRequestDTO();
        request.setUsuarioId(1);
        request.setModuloId(10);
        request.setJustificativa("Preciso de acesso");
        request.setQtdDias(5);

        // Preparamos o que os Mocks vão devolver quando forem chamados
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(new Usuario()));
        when(moduloRepository.findById(10)).thenReturn(Optional.of(new Modulo()));
        
        Solicitacao solicitacaoMapeada = new Solicitacao();
        when(solicitacaoMapper.toEntity(request)).thenReturn(solicitacaoMapeada);
        
        Solicitacao solicitacaoSalva = new Solicitacao();
        solicitacaoSalva.setId(100);
        solicitacaoSalva.setStatus(SolicitacaoStatus.PENDENTE);
        
        // Quando salvar, retorna a solicitação com ID
        when(solicitacaoRepository.save(any(Solicitacao.class))).thenReturn(solicitacaoSalva);
        
        SolicitacaoResponseDTO responseEsperada = new SolicitacaoResponseDTO();
        responseEsperada.setId(100);
        when(solicitacaoMapper.toDTO(solicitacaoSalva)).thenReturn(responseEsperada);

        // ==========================================
        // ACT (Agir)
        // ==========================================
        SolicitacaoResponseDTO resultado = solicitacaoService.criarSolicitacao(request);

        // ==========================================
        // ASSERT (Verificar)
        // ==========================================
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(100);
        verify(solicitacaoRepository, times(1)).save(any(Solicitacao.class)); // Garante que mandou salvar no banco
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        // ==========================================
        // ARRANGE
        // ==========================================
        SolicitacaoRequestDTO request = new SolicitacaoRequestDTO();
        request.setUsuarioId(999); // ID falso
        request.setModuloId(10);

        // Ensina o banco a não encontrar o usuário
        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        // ==========================================
        // ACT & ASSERT
        // ==========================================
        assertThatThrownBy(() -> solicitacaoService.criarSolicitacao(request))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Usuário não encontrado");

        // Verifica que o sistema abortou e NUNCA tentou salvar no banco
        verify(solicitacaoRepository, never()).save(any());

        
    }
@Test
    void deveLancarExcecaoQuandoModuloNaoExistir() {
        // ARRANGE
        SolicitacaoRequestDTO request = new SolicitacaoRequestDTO();
        request.setUsuarioId(1);
        request.setModuloId(999); // ID de módulo falso

        // O usuário existe, mas o módulo não
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(new Usuario()));
        when(moduloRepository.findById(999)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> solicitacaoService.criarSolicitacao(request))
                .isInstanceOf(EntityNotFoundException.class); // Isso cobre a sua "lambda$1" do JaCoCo!

        // Garante que a solicitação não foi salva
        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void deveListarTodasAsSolicitacoes() {
        // ARRANGE
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(1);
        when(solicitacaoRepository.findAll()).thenReturn(List.of(solicitacao));
        
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO();
        dto.setId(1);
        when(solicitacaoMapper.toDTO(solicitacao)).thenReturn(dto);

        // ACT
        List<SolicitacaoResponseDTO> resultado = solicitacaoService.listarTodas();

        // ASSERT
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getId()).isEqualTo(1);
        verify(solicitacaoRepository, times(1)).findAll();
    }

    @Test
    void deveListarSolicitacoesPorUsuario() {
        // ARRANGE
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setId(2);
        when(solicitacaoRepository.findByUsuarioId(1)).thenReturn(List.of(solicitacao));
        
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO();
        dto.setId(2);
        when(solicitacaoMapper.toDTO(solicitacao)).thenReturn(dto);

        // ACT
        List<SolicitacaoResponseDTO> resultado = solicitacaoService.listarPorUsuario(1);

        // ASSERT
        assertThat(resultado).isNotEmpty();
        assertThat(resultado.get(0).getId()).isEqualTo(2);
        verify(solicitacaoRepository, times(1)).findByUsuarioId(1);
    }

}