package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.model.*;
import br.com.gatekeeper.controle_acessos.model.enums.StatusHistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.enums.StatusSolicitacao;
import br.com.gatekeeper.controle_acessos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ParecerService {

    @Autowired private ParecerRepository parecerRepository;
    @Autowired private SolicitacaoRepository solicitacaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private HistoricoAcessoRepository historicoRepository;
    @Autowired private NotificacaoRepository notificacaoRepository;

    // O @Transactional garante que se der erro no meio (ex: ao criar a notificação), 
    // ele desfaz a aprovação para não deixar o banco inconsistente.
    @Transactional 
    public ParecerResponseDTO avaliarSolicitacao(ParecerRequestDTO request) {
        
        Solicitacao solicitacao = solicitacaoRepository.findById(request.getSolicitacaoId())
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        Usuario avaliador = usuarioRepository.findById(request.getUsuarioResponsavelId())
                .orElseThrow(() -> new RuntimeException("Avaliador não encontrado"));

        // 1. Cria e salva o Parecer
        Parecer parecer = new Parecer();
        parecer.setDescricao(request.getDescricao());
        parecer.setDecisao(request.getDecisao().toUpperCase());
        parecer.setDataParecer(LocalDateTime.now());
        parecer.setSolicitacao(solicitacao);
        parecer.setUsuarioResponsavel(avaliador);
        parecer = parecerRepository.save(parecer);

        // 2. Atualiza o status da Solicitação
        if (parecer.getDecisao().equals("APROVADA")) {
            solicitacao.setStatus(StatusSolicitacao.APROVADA);
            
            // 3. Cria o Histórico de Acesso automaticamente!
            HistoricoAcesso historico = new HistoricoAcesso();
            historico.setDataInicio(LocalDateTime.now());
            historico.setStatus(StatusHistoricoAcesso.ATIVO);
            historico.setUsuario(solicitacao.getUsuario());
            historico.setModulo(solicitacao.getModulo());
            historicoRepository.save(historico);
            
        } else {
            solicitacao.setStatus(StatusSolicitacao.REJEITADA);
        }
        solicitacaoRepository.save(solicitacao);

        // 4. Cria a Notificação para o usuário que pediu o acesso
        Notificacao notificacao = new Notificacao();
        notificacao.setMensagem("Sua solicitação " + solicitacao.getProtocolo() + " foi " + parecer.getDecisao());
        notificacao.setDataEnvio(LocalDateTime.now());
        notificacao.setDecisao(parecer.getDecisao());
        notificacao.setSolicitacao(solicitacao);
        notificacao.setUsuario(solicitacao.getUsuario());
        notificacaoRepository.save(notificacao);

        // 5. Devolve o DTO de resposta
        ParecerResponseDTO response = new ParecerResponseDTO();
        response.setId(parecer.getId());
        response.setDescricao(parecer.getDescricao());
        response.setDecisao(parecer.getDecisao());
        response.setDataParecer(parecer.getDataParecer());
        response.setNomeAvaliador(avaliador.getNome());
        
        return response;
    }
}