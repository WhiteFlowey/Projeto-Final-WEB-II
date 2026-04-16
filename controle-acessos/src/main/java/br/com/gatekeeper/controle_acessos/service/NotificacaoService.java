package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.response.NotificacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.NotificacaoMapper;
import br.com.gatekeeper.controle_acessos.model.Notificacao;
import br.com.gatekeeper.controle_acessos.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    @Autowired
    private NotificacaoMapper mapper; // 1. O Mapper entra em ação aqui

    // 2. Agora retornamos a lista de DTOs limpa e segura
    public List<NotificacaoResponseDTO> buscarNotificacoesDoUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}