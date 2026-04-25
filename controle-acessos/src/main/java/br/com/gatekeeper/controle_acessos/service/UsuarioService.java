package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.UsuarioMapper;
import br.com.gatekeeper.controle_acessos.model.*;
import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import br.com.gatekeeper.controle_acessos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private DepartamentoRepository departamentoRepository;
    @Autowired private PerfilRepository perfilRepository;
    @Autowired private UsuarioMapper usuarioMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        
        // Criptografia e dependências
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setDepartamento(departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado")));
        usuario.setPerfil(perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado")));
        usuario.setStatus(UsuarioStatus.ATIVO);

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDTO).toList();
    }

    // ➕ Novo: Lógica para buscar por ID
    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return usuarioMapper.toDTO(usuario);
    }

    // ➕ Novo: Lógica para Atualizar
    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Integer id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setRegistroEmpregado(request.getRegistroEmpregado()); 
        
        if (request.getSenha() != null && !request.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    // ➕ Novo: Lógica para Remover
    @Transactional
    public void removerUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}