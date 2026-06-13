package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.UsuarioMapper;
import br.com.gatekeeper.controle_acessos.model.*;
import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import br.com.gatekeeper.controle_acessos.model.vo.Email; 
import br.com.gatekeeper.controle_acessos.repository.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final DepartamentoRepository departamentoRepository;
    private final PerfilRepository perfilRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    UsuarioService(UsuarioRepository usuarioRepository, DepartamentoRepository departamentoRepository, PerfilRepository perfilRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.departamentoRepository = departamentoRepository;
        this.perfilRepository = perfilRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setDepartamento(departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado")));
        usuario.setPerfil(perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado")));
        usuario.setStatus(UsuarioStatus.ATIVO);

        String anoAtual = String.valueOf(java.time.Year.now().getValue());
        int sequencialMock = new java.util.Random().nextInt(90) + 10; 
        usuario.setRegistroEmpregado("RE-" + anoAtual + "-TST-" + sequencialMock);

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Cacheable("usuarios")
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(usuarioMapper::toDTO).toList();
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        // 1. Busca o usuário solicitado no banco de dados
        Usuario usuarioBuscado = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 2. Pega as informações de quem fez a requisição (quem está logado no Token)
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = authentication.getName(); 
        
        // 3. Verifica se a pessoa logada tem o poder de ADMIN
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        // 4. A REGRA DE BLINDAGEM
        // Como usamos VO (Value Object) para o Email, precisamos extrair a String dele.
        String emailDoUsuarioBuscado = usuarioBuscado.getEmail().getEndereco(); 

        if (!isAdmin && !emailDoUsuarioBuscado.equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Acesso negado: Você só pode visualizar o seu próprio perfil.");
        }

        // 5. Se passou pela barreira, devolve o DTO
        return usuarioMapper.toDTO(usuarioBuscado);
    }

    @Transactional
    public UsuarioResponseDTO atualizarUsuario(Integer id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        usuario.setNome(request.getNome());
        usuario.setEmail(new Email(request.getEmail()));
              
        if (request.getSenha() != null && !request.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        }

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void removerUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}