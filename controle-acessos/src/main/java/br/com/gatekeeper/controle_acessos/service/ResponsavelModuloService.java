package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.dto.request.ResponsavelModuloRequestDTO;
import br.com.gatekeeper.controle_acessos.mapper.ResponsavelModuloMapper;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.ResponsavelModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ResponsavelModuloService {

    @Autowired
    private ResponsavelModuloRepository repository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private ResponsavelModuloMapper mapper;

    public ResponsavelModuloDTO definirResponsavel(ResponsavelModuloRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Modulo modulo = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado"));

        ResponsavelModulo responsavel = new ResponsavelModulo();
        responsavel.setUsuario(usuario);
        responsavel.setModulo(modulo);
        responsavel.setDataInicio(LocalDateTime.now());

        responsavel = repository.save(responsavel);

        return mapper.toDTO(responsavel);
    }

    public List<ResponsavelModuloDTO> listarPorModulo(Integer moduloId) {
        return repository.findByModuloId(moduloId).stream()
                .map(mapper::toDTO)
                .toList();
    }

    public List<ResponsavelModuloDTO> listarTodos() {
    return repository.findAll().stream()
            .map(mapper::toDTO)
            .toList();
}
}