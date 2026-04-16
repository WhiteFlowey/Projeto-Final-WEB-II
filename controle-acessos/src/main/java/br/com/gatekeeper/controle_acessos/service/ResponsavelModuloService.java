package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.mapper.ResponsavelModuloMapper;
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
    
    @Autowired 
    private ResponsavelModuloMapper mapper;

    public ResponsavelModuloDTO definirResponsavel(ResponsavelModulo responsavel) {
        responsavel.setDataInicio(LocalDateTime.now());
        responsavel = repository.save(responsavel);
        return mapper.toDTO(responsavel);
    }

    public List<ResponsavelModuloDTO> listarPorModulo(Integer moduloId) {
        return repository.findByModuloId(moduloId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}