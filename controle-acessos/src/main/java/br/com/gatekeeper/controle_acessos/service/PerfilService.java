package br.com.gatekeeper.controle_acessos.service;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.mapper.PerfilMapper;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
// Define que essa classe é uma camada de Service (regras de negócio)
public class PerfilService {
    // Responsável pelo acesso ao banco de dados
    private final PerfilRepository repository;
    // Responsável pela conversão Entity ↔ DTO
    private final PerfilMapper mapper;
    PerfilService(PerfilRepository repository, PerfilMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    } 

    public PerfilDTO salvar(PerfilDTO dto) {
        // Converte DTO para entidade
        Perfil perfil = mapper.toEntity(dto);
         // Salva no banco de dados
        perfil = repository.save(perfil);
        // Converte de volta para DTO e retorna
        return mapper.toDTO(perfil);
    }

    @Cacheable("perfis") // Armazena o resultado em cache para melhorar performance
    public List<PerfilDTO> listarTodos() {
         // Busca todos os perfis no banco, converte para DTO e retorna a lista
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PerfilDTO atualizar(Integer id, PerfilDTO dto) {
        // 1. Busca a entidade existente no banco pelo ID
        Perfil perfil = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado"));

        // 2. Atualiza os dados da entidade usando as informações que vieram do DTO
        perfil.setNome(dto.getNome());
        perfil.setDescricao(dto.getDescricao());

        // 3. Salva a entidade atualizada de volta no banco
        Perfil perfilAtualizado = repository.save(perfil);

        // 4. Converte a entidade salva para DTO antes de retornar para o Controller
        return mapper.toDTO(perfilAtualizado);
    }
}