package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.ParecerMapper;
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
    
    @Autowired private ParecerMapper parecerMapper;

    @Transactional 
    public ParecerResponseDTO avaliarSolicitacao(ParecerRequestDTO request) {
        
        Solicitacao solicitacao = solicitacaoRepository.findById(request.getSolicitacaoId())
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        Usuario avaliador = usuarioRepository.findById(request.getUsuarioResponsavelId())
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));

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