package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.mapper.PerfilMapper;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PerfilService {

    private final PerfilRepository repository;
    private final PerfilMapper mapper;

    PerfilService(PerfilRepository repository, PerfilMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    } 

    public PerfilDTO salvar(PerfilDTO dto) {
        Perfil perfil = mapper.toEntity(dto);
        perfil = repository.save(perfil);
        return mapper.toDTO(perfil);
    }

    @Cacheable("perfis")
    public List<PerfilDTO> listarTodos() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PerfilDTO atualizar(Integer id, PerfilDTO dto) {
        // 1. Busca a entidade existente no banco pelo ID
        Perfil perfil = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        // 2. Atualiza os dados da entidade usando as informações que vieram do DTO
        perfil.setNome(dto.getNome());
        perfil.setDescricao(dto.getDescricao());

        // 3. Salva a entidade atualizada de volta no banco
        Perfil perfilAtualizado = repository.save(perfil);

        // 4. Converte a entidade salva para DTO antes de retornar para o Controller
        return mapper.toDTO(perfilAtualizado);
    }
}