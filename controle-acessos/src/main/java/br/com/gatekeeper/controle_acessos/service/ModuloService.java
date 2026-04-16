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

    public ModuloDTO salvar(ModuloDTO dto) {
        // Converte o DTO para Entidade, salva e devolve como DTO
        Modulo modulo = mapper.toEntity(dto);
        modulo = repository.save(modulo);
        return mapper.toDTO(modulo);
    }

    public List<ModuloDTO> listarTodos() {
        // Listagem limpa em uma linha
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}