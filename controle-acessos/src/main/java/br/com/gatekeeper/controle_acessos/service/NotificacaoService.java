package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.NotificacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Notificacao;
import br.com.gatekeeper.controle_acessos.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    // Retorna as notificações mastigadas (em DTO) para o "sininho" do aplicativo
    public List<NotificacaoResponseDTO> buscarNotificacoesDoUsuario(Integer usuarioId) {
        
        // 1. Busca no banco de dados usando a query personalizada que criamos
        List<Notificacao> notificacoesSalvas = repository.buscarTodasDoUsuario(usuarioId);
        
        // 2. Converte a lista do Banco para uma lista de DTOs usando o método abaixo
        return notificacoesSalvas.stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    // O nosso método de conversão seguro (que criamos na mensagem anterior)
    private NotificacaoResponseDTO converterParaResponseDTO(Notificacao notificacao) {
        NotificacaoResponseDTO dto = new NotificacaoResponseDTO();
        
        dto.setId(notificacao.getId());
        dto.setMensagem(notificacao.getMensagem());
        dto.setDataEnvio(notificacao.getDataEnvio());
        dto.setDecisao(notificacao.getDecisao());

        // Navegação segura para pegar o ID do Parecer e o Protocolo
        if (notificacao.getParecer() != null) {
            dto.setParecerId(notificacao.getParecer().getId());

            if (notificacao.getParecer().getSolicitacao() != null) {
                dto.setProtocoloSolicitacao(notificacao.getParecer().getSolicitacao().getProtocolo());
            }
        }

        return dto;
    }
}