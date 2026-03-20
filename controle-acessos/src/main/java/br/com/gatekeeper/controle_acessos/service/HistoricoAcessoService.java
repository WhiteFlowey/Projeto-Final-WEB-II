package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.repository.HistoricoAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistoricoAcessoService {

    @Autowired
    private HistoricoAcessoRepository repository;

    // Retorna todo o histórico de um usuário específico para mostrar na tela dele
    public List<HistoricoAcesso> buscarHistoricoDoUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }
}