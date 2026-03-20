package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PerfilService {

    @Autowired
    private PerfilRepository repository;

    public Perfil salvar(Perfil perfil) {
        return repository.save(perfil);
    }

    public List<Perfil> listarTodos() {
        return repository.findAll();
    }
}