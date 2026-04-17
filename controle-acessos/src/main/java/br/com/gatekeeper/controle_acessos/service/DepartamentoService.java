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

    // Atualiza um departamento que já existe
    public DepartamentoDTO atualizar(Integer id, DepartamentoDTO dto) {
        // 1. Procura o departamento pelo ID no banco de dados
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com o ID: " + id));

        // 2. Atualiza os dados antigos com os dados novos que vieram do Postman
        departamento.setNome(dto.getNome());
        // Se a sua entidade Departamento tiver outros campos, atualize eles aqui também!

        // 3. Salva a alteração no banco e devolve como DTO
        departamento = repository.save(departamento);
        return mapper.toDTO(departamento);
    }

    // Deleta um departamento pelo ID
    public void deletar(Integer id) {
        // 1. Verifica se o departamento realmente existe antes de tentar apagar
        Departamento departamento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado com o ID: " + id));

        // 2. Apaga do banco de dados
        repository.delete(departamento);
    }
}