package br.com.gatekeeper.controle_acessos.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.dto.request.ResponsavelModuloRequestDTO;
import br.com.gatekeeper.controle_acessos.mapper.ResponsavelModuloMapper;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.ResponsavelModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

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
                .orElseThrow(() -> new EntityNotFoundException("Usuário não existe!"));
        Modulo mod = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new EntityNotFoundException("Módulo não existe!"));

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