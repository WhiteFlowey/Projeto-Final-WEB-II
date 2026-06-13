package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.dto.request.ResponsavelModuloRequestDTO;
import br.com.gatekeeper.controle_acessos.mapper.ResponsavelModuloMapper;
import br.com.gatekeeper.controle_acessos.model.*;
import br.com.gatekeeper.controle_acessos.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponsavelModuloService {

    private final ResponsavelModuloRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final ModuloRepository moduloRepository;
    private final ResponsavelModuloMapper mapper;

    ResponsavelModuloService(ResponsavelModuloRepository repository, UsuarioRepository usuarioRepository, ModuloRepository moduloRepository, ResponsavelModuloMapper mapper) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
        this.mapper = mapper;
    }

    //
    public ResponsavelModuloDTO atribuirNovoDiretor(ResponsavelModuloRequestDTO dto) {
        Usuario usu = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não existe!"));
        Modulo mod = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não existe!"));

        ResponsavelModulo novo = new ResponsavelModulo();
        novo.setUsuario(usu);
        novo.setModulo(mod);
        // Ao atribuir um novo responsável, o sistema automaticamente registra a data de início da responsabilidade
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