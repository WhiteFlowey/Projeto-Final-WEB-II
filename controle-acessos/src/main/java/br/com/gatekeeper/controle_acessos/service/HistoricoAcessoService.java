package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.HistoricoAcessoMapper;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.repository.HistoricoAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoricoAcessoService {

    @Autowired
    private HistoricoAcessoRepository repository;

    @Autowired
    private HistoricoAcessoMapper mapper; // 1. Injetando o Mapper

    // 2. Retornando os DTOs mapeados numa única linha
    public List<HistoricoAcessoResponseDTO> buscarHistoricoDoUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}