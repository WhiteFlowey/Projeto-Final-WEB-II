package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.DepartamentoDTO;
import br.com.gatekeeper.controle_acessos.mapper.DepartamentoMapper;
import br.com.gatekeeper.controle_acessos.model.Departamento;
import br.com.gatekeeper.controle_acessos.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    @Autowired
    private DepartamentoMapper mapper; // Injetando o mapper

    public DepartamentoDTO salvar(DepartamentoDTO dto) {
        // Converte o DTO para Entidade, salva no banco e devolve como DTO
        Departamento departamento = mapper.toEntity(dto);
        departamento = repository.save(departamento);
        return mapper.toDTO(departamento);
    }

    public List<DepartamentoDTO> listarTodos() {
        // Busca todos e converte cada um para DTO
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .toList();
    }
}