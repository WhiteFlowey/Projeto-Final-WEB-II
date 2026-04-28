package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.dto.request.ResponsavelModuloRequestDTO;
import br.com.gatekeeper.controle_acessos.mapper.ResponsavelModuloMapper;
import br.com.gatekeeper.controle_acessos.model.*;
import br.com.gatekeeper.controle_acessos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponsavelModuloService {

    @Autowired private ResponsavelModuloRepository repository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ModuloRepository moduloRepository;
    @Autowired private ResponsavelModuloMapper mapper;

    //
    public ResponsavelModuloDTO atribuirNovoDiretor(ResponsavelModuloRequestDTO dto) {
        Usuario usu = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não existe!"));
        Modulo mod = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não existe!"));

        ResponsavelModulo novo = new ResponsavelModulo();
        novo.setUsuario(usu);
        novo.setModulo(mod);
        novo.setDataInicio(LocalDateTime.now());
        
        return mapper.toDTO(repository.save(novo));
    }

    public List<ResponsavelModuloDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    public List<ResponsavelModuloDTO> listarPorNome(String nome) {
        return repository.findByUsuarioNomeContainingIgnoreCase(nome)
                .stream().map(mapper::toDTO).toList();
    }
}