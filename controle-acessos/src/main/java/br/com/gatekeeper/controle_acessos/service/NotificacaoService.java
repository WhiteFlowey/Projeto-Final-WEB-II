package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.response.NotificacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.NotificacaoMapper;
import br.com.gatekeeper.controle_acessos.repository.NotificacaoRepository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository repository;

    private final NotificacaoMapper mapper;

    NotificacaoService(NotificacaoRepository repository, NotificacaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    } 

    // 2. Agora retornamos a lista de DTOs limpa e segura
    // 👇 O cache cria uma "gaveta" específica para cada ID de usuário
    @Cacheable(value = "notificacoes", key = "#usuarioId")
    public List<NotificacaoResponseDTO> buscarNotificacoesDoUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}