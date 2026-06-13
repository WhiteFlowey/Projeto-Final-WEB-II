package br.com.gatekeeper.controle_acessos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;
    
    // 2. Injetar o Mapper
    private final SolicitacaoMapper solicitacaoMapper;

    SolicitacaoService(SolicitacaoRepository solicitacaoRepository, UsuarioRepository usuarioRepository, ModuloRepository moduloRepository, SolicitacaoMapper solicitacaoMapper) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
        this.solicitacaoMapper = solicitacaoMapper;
    }

    @Transactional
    public SolicitacaoResponseDTO criarSolicitacao(SolicitacaoRequestDTO request) {
        
        // Validações continuam aqui (é regra de negócio!)
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado. ID: " + request.getUsuarioId()));

        Modulo modulo = moduloRepository.findById(request.getModuloId())
                .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado. ID: " + request.getModuloId()));

        // 3. O Mapper cria a entidade (ele ignora usuario e modulo conforme configuramos)
        Solicitacao solicitacao = solicitacaoMapper.toEntity(request);

        // 4. Regras Automáticas continuam aqui
        solicitacao.setUsuario(usuario);
        solicitacao.setModulo(modulo);
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        solicitacao.setStatus(SolicitacaoStatus.PENDENTE);
        solicitacao.setProtocolo(gerarProtocolo());

        solicitacao = solicitacaoRepository.save(solicitacao);

        // 5. Retorno direto pelo Mapper (Adeus converterParaResponseDTO!)
        return solicitacaoMapper.toDTO(solicitacao);
    }

    private String gerarProtocolo() {
        int ano = LocalDateTime.now().getYear();
        String codigoAleatorio = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "REQ-" + ano + "-" + codigoAleatorio;
    }
    
    //LISTAR TODAS (Para o Admin)
    public List<SolicitacaoResponseDTO> listarTodas() {
        return solicitacaoRepository.findAll().stream()
                .map(solicitacaoMapper::toDTO)
                .toList();
    }

    //LISTAR POR USUÁRIO (Para a tela "Minhas Solicitações" do Colaborador)
    public List<SolicitacaoResponseDTO> listarPorUsuario(Integer usuarioId) {
        // Atenção: Você precisará criar o método findByUsuarioId no SolicitacaoRepository!
        return solicitacaoRepository.findByUsuarioId(usuarioId).stream()
                .map(solicitacaoMapper::toDTO)
                .toList();
    }
  
}