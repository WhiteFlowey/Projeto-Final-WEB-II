package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.Departamento;
import br.com.gatekeeper.controle_acessos.repository.DepartamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartamentoService {

    @Autowired
    private DepartamentoRepository repository;

    public Departamento salvar(Departamento departamento) {
        return repository.save(departamento);
    }

    public List<Departamento> listarTodos() {
        return repository.findAll();
    }
}