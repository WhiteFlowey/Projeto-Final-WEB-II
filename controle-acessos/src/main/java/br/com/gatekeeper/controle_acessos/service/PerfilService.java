package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.mapper.PerfilMapper;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository repository;

    @Autowired
    private PerfilMapper mapper; // Injetamos o mapper

    public PerfilDTO salvar(PerfilDTO dto) {
        // 1. Converte o DTO que veio da tela para Entidade
        Perfil perfil = mapper.toEntity(dto);
        
        // 2. Salva no banco
        perfil = repository.save(perfil);
        
        // 3. Devolve como DTO novamente
        return mapper.toDTO(perfil);
    }

    public List<PerfilDTO> listarTodos() {
        // Listagem numa única linha usando o mapper!
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}