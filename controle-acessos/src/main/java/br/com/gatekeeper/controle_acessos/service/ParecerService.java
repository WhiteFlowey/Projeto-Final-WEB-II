package br.com.gatekeeper.controle_acessos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gatekeeper.controle_acessos.dto.request.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.ParecerMapper;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.Notificacao;
import br.com.gatekeeper.controle_acessos.model.Parecer;
import br.com.gatekeeper.controle_acessos.model.Solicitacao;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.HistoricoAcessoStatus;
import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import br.com.gatekeeper.controle_acessos.repository.HistoricoAcessoRepository;
import br.com.gatekeeper.controle_acessos.repository.NotificacaoRepository;
import br.com.gatekeeper.controle_acessos.repository.ParecerRepository;
import br.com.gatekeeper.controle_acessos.repository.SolicitacaoRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ParecerService {

    private final ParecerRepository parecerRepository;
    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final HistoricoAcessoRepository historicoRepository;
    private final NotificacaoRepository notificacaoRepository;
    
    private final ParecerMapper parecerMapper;

    ParecerService(ParecerRepository parecerRepository, SolicitacaoRepository solicitacaoRepository, UsuarioRepository usuarioRepository, HistoricoAcessoRepository historicoRepository, NotificacaoRepository notificacaoRepository, ParecerMapper parecerMapper) {
        this.parecerRepository = parecerRepository;
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.historicoRepository = historicoRepository;
        this.notificacaoRepository = notificacaoRepository;
        this.parecerMapper = parecerMapper;
    }


    @Transactional 

    public ParecerResponseDTO avaliarSolicitacao(ParecerRequestDTO request) {
        
        Solicitacao solicitacao = solicitacaoRepository.findById(request.getSolicitacaoId())
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada"));

        Usuario avaliador = usuarioRepository.findById(request.getUsuarioResponsavelId())
                .orElseThrow(() -> new EntityNotFoundException("Avaliador não encontrado"));

        Parecer parecer = parecerMapper.toEntity(request);
        
        parecer.setDataParecer(LocalDateTime.now());
        parecer.setSolicitacao(solicitacao);
        parecer.setUsuarioResponsavel(avaliador);
        parecer = parecerRepository.save(parecer);

        // Preparamos o Histórico base (comum para Aprovado ou Negado)
        HistoricoAcesso historico = new HistoricoAcesso();
        historico.setDataInicio(LocalDateTime.now());
        historico.setUsuario(solicitacao.getUsuario());
        historico.setModulo(solicitacao.getModulo());

        // Fluxo de Decisão Automática
        if (parecer.getDecisao().equalsIgnoreCase("APROVADA")) {
            solicitacao.setStatus(SolicitacaoStatus.APROVADA);
            
            historico.setStatus(HistoricoAcessoStatus.ATIVO);
            // Adiciona a quantidade de dias solicitados na data de hoje
            historico.setDataFim(LocalDateTime.now().plusDays(solicitacao.getQtdDias()));
            
        } else {
            solicitacao.setStatus(SolicitacaoStatus.REJEITADA);
            
            // Registra no histórico que a tentativa existiu, mas foi barrada
            historico.setStatus(HistoricoAcessoStatus.NEGADO);
            historico.setDataFim(LocalDateTime.now()); // Encerra o prazo no mesmo segundo
        }
        
        historicoRepository.save(historico);
        solicitacaoRepository.save(solicitacao);

        // Notificação Automática
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem("Sua solicitação " + solicitacao.getProtocolo() + " foi " + parecer.getDecisao());
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setDecisao(parecer.getDecisao());        
        notificacao.setParecer(parecer); 
        
        notificacaoRepository.save(notificacao);

        return parecerMapper.toDTO(parecer);
    }

    
    public List<ParecerResponseDTO> listarTodos() {
        return parecerRepository.findAll()
                .stream()
                .map(parecerMapper::toDTO)
                .toList();
    }
}