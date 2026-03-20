package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.Notificacao;
import br.com.gatekeeper.controle_acessos.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository repository;

    // Retorna as notificações de um usuário para mostrar no "sininho" do sistema
    public List<Notificacao> buscarNotificacoesDoUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}