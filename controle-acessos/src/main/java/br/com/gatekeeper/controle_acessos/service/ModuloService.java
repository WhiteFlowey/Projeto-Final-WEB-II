package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository repository;

    public Modulo salvar(Modulo modulo) {
        return repository.save(modulo);
    }

    public List<Modulo> listarTodos() {
        return repository.findAll();
    }
}