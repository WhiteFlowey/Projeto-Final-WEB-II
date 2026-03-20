package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import br.com.gatekeeper.controle_acessos.repository.ResponsavelModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponsavelModuloService {

    @Autowired
    private ResponsavelModuloRepository repository;

    public ResponsavelModulo definirResponsavel(ResponsavelModulo responsavel) {
        responsavel.setDataInicio(LocalDateTime.now());
        return repository.save(responsavel);
    }

    public List<ResponsavelModulo> listarPorModulo(Integer moduloId) {
        return repository.findByModuloId(moduloId);
    }
}