package br.com.gatekeeper.controle_acessos.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final SolicitacaoMapper solicitacaoMapper;

    SolicitacaoService(SolicitacaoRepository solicitacaoRepository, UsuarioRepository usuarioRepository, ModuloRepository moduloRepository, SolicitacaoMapper solicitacaoMapper) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
        this.solicitacaoMapper = solicitacaoMapper;
    }

    @Transactional
    public SolicitacaoResponseDTO criarSolicitacao(SolicitacaoRequestDTO request) {
        
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado. ID: " + request.getUsuarioId()));

        Modulo modulo = moduloRepository.findById(request.getModuloId())
                .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado. ID: " + request.getModuloId()));

        Solicitacao solicitacao = solicitacaoMapper.toEntity(request);

        solicitacao.setUsuario(usuario);
        solicitacao.setModulo(modulo);
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        solicitacao.setStatus(SolicitacaoStatus.PENDENTE);
        solicitacao.setProtocolo(gerarProtocolo());

        solicitacao = solicitacaoRepository.save(solicitacao);

        return solicitacaoMapper.toDTO(solicitacao);
    }

    private String gerarProtocolo() {
        int ano = LocalDateTime.now().getYear();
        String codigoAleatorio = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "REQ-" + ano + "-" + codigoAleatorio;
    }
    
    public List<SolicitacaoResponseDTO> listarTodas() {
        return solicitacaoRepository.findAll().stream()
                .map(solicitacaoMapper::toDTO)
                .toList();
    }

    public List<SolicitacaoResponseDTO> listarPorUsuario(Integer usuarioId) {
        usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário com ID " + usuarioId + " não foi encontrado no sistema."));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailDoUsuarioLogado = authentication.getName();

        // 1. Recebe o UserDetails diretamente
        var userDetails = usuarioRepository.findByEmail(emailDoUsuarioLogado);
        
        // 2. Faz a checagem de nulo na mão
        if (userDetails == null) {
            throw new AccessDeniedException("Token inválido ou usuário não encontrado.");
        }
        
        // 3. Converte (cast) para a sua classe Usuario
        Usuario usuarioLogado = (Usuario) userDetails;

       boolean isAdmin = false;
        for (var authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }

        if (!isAdmin && !usuarioLogado.getId().equals(usuarioId)) {
            throw new AccessDeniedException("Acesso Negado: Você só pode visualizar as suas próprias solicitações.");
        }

        return solicitacaoRepository.findByUsuarioId(usuarioId).stream()
                .map(solicitacaoMapper::toDTO)
                .toList();
    }
}