package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.ParecerMapper; // 1. Importar o mapper
import br.com.gatekeeper.controle_acessos.model.*;
import br.com.gatekeeper.controle_acessos.model.enums.HistoricoAcessoStatus;
import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import br.com.gatekeeper.controle_acessos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParecerService {

    @Autowired private ParecerRepository parecerRepository;
    @Autowired private SolicitacaoRepository solicitacaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private HistoricoAcessoRepository historicoRepository;
    @Autowired private NotificacaoRepository notificacaoRepository;
    
    // 2. Injetar o Mapper
    @Autowired private ParecerMapper parecerMapper;

    @Transactional 
    public ParecerResponseDTO avaliarSolicitacao(ParecerRequestDTO request) {
        
        Solicitacao solicitacao = solicitacaoRepository.findById(request.getSolicitacaoId())
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        Usuario avaliador = usuarioRepository.findById(request.getUsuarioResponsavelId())
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));

        // 3. O Mapper cria a entidade baseada no request
        Parecer parecer = parecerMapper.toEntity(request);
        
        // 4. Regras de Negócio e Relacionamentos
        parecer.setDataParecer(LocalDateTime.now());
        parecer.setSolicitacao(solicitacao);
        parecer.setUsuarioResponsavel(avaliador);
        parecer = parecerRepository.save(parecer);

        // 5. Fluxo de Decisão Automática
        if (parecer.getDecisao().equalsIgnoreCase("APROVADA")) {
            solicitacao.setStatus(SolicitacaoStatus.APROVADA);
            
            // Criação do histórico (Lógica de Negócio permanece aqui)
            HistoricoAcesso historico = new HistoricoAcesso();
            historico.setDataInicio(LocalDateTime.now());
            historico.setStatus(HistoricoAcessoStatus.ATIVO);
            historico.setUsuario(solicitacao.getUsuario());
            historico.setModulo(solicitacao.getModulo());
            historicoRepository.save(historico);
            
        } else {
            solicitacao.setStatus(SolicitacaoStatus.REJEITADA);
        }
        solicitacaoRepository.save(solicitacao);

        // 6. Notificação Automática
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem("Sua solicitação " + solicitacao.getProtocolo() + " foi " + parecer.getDecisao());
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setDecisao(parecer.getDecisao());        
        notificacao.setParecer(parecer); 
        
        notificacaoRepository.save(notificacao);

        // 7. Devolve o DTO mapeado (Limpo e profissional)
        return parecerMapper.toDTO(parecer);
    }

    public List<ParecerResponseDTO> listarTodos() {
    return parecerRepository.findAll()
            .stream()
            .map(parecerMapper::toDTO)
            .toList();
}
}