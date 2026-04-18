package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.UsuarioMapper;
import br.com.gatekeeper.controle_acessos.model.Departamento;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import br.com.gatekeeper.controle_acessos.repository.DepartamentoRepository;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private UsuarioMapper usuarioMapper; // Injetando o seu novo Mapper

    @Autowired
    private PasswordEncoder passwordEncoder; // Injetando o codificador definido na SecurityConfigurations

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO request) {
        // 1. Validação de Dependências (Regra de Negócio)
        Departamento departamento = departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        // 2. Uso do Mapper para converter DTO em Entidade
        // O Mapper já cuida de campos simples como nome e email
        Usuario usuario = usuarioMapper.toEntity(request);

        // 3. Segurança: Criptografando a senha antes de salvar
        String senhaCriptografada = passwordEncoder.encode(request.getSenha());
        usuario.setSenha(senhaCriptografada);

        // 4. Configurando as relações e status manualmente (pois vêm de IDs específicos)
        usuario.setDepartamento(departamento);
        usuario.setPerfil(perfil);
        usuario.setStatus(UsuarioStatus.valueOf(request.getStatus().toUpperCase()));

        // 5. Persistência
        usuario = usuarioRepository.save(usuario);

        // 6. Retorno usando o Mapper para converter Entidade em ResponseDTO
        return usuarioMapper.toDTO(usuario);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        // O Stream agora fica muito mais curto usando a referência do método do Mapper
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }
}