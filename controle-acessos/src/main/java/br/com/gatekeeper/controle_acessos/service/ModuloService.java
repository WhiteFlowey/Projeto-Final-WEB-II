package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.ModuloDTO;
import br.com.gatekeeper.controle_acessos.mapper.ModuloMapper;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository repository;

    @Autowired
    private ModuloMapper mapper; // 1. Injetando o nosso Mapper

    public ModuloDTO criarModulo(ModuloDTO dto) {
        // Converte o DTO para Entidade, salva e devolve como DTO
        Modulo modulo = mapper.toEntity(dto);
        modulo = repository.save(modulo);
        return mapper.toDTO(modulo);
    }

    public List<ModuloDTO> listarTodosModulos() {
        // Listagem limpa em uma linha
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public ModuloDTO atualizar(Integer id, ModuloDTO dto) {
        // Busca o módulo existente no banco de dados
        Modulo moduloExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado com o ID: " + id));

        // Atualiza apenas os campos que vieram preenchidos no DTO
        if (dto.getNome() != null && !dto.getNome().isEmpty()) {
            moduloExistente.setNome(dto.getNome());
        }
        if (dto.getDescricao() != null) {
            moduloExistente.setDescricao(dto.getDescricao());
        }

        // Salva no banco 
        moduloExistente = repository.save(moduloExistente);

        // converte de volta para DTO para mostrar na resposta
        return mapper.toDTO(moduloExistente);
    }

    public void deletar(Integer id) {
        // Verifica se existe antes de tentar deletar
        if (!repository.existsById(id)) {
            throw new RuntimeException("Módulo não encontrado com o ID: " + id);
        }
        repository.deleteById(id);
    }

    // 5. BUSCAR POR ID (LER UM)
    public ModuloDTO buscarPorId(Integer id) {
        Modulo modulo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado com o ID: " + id));
        
        return mapper.toDTO(modulo);
    }

    
}