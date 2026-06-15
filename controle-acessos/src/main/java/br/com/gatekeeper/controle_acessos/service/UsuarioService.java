package br.com.gatekeeper.controle_acessos.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.UsuarioMapper;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import br.com.gatekeeper.controle_acessos.model.vo.Email;
import br.com.gatekeeper.controle_acessos.repository.DepartamentoRepository;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

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
    @CacheEvict(value = "usuarios", allEntries = true)
    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setDepartamento(departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Departamento não encontrado")));
        usuario.setPerfil(perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new EntityNotFoundException("Perfil não encontrado")));
        usuario.setStatus(UsuarioStatus.ATIVO);

        String anoAtual = String.valueOf(java.time.Year.now().getValue());
        int sequencialMock = new java.util.Random().nextInt(90) + 10; 
        usuario.setRegistroEmpregado("RE-" + anoAtual + "-TST-" + sequencialMock);

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }

    @Cacheable("usuarios")
    @CacheEvict(value = "usuarios", allEntries = true) // Limpa o cache ao atualizar!
    public Page<UsuarioResponseDTO> listarTodos(Pageable paginacao) {
        return usuarioRepository.findAll(paginacao).map(usuarioMapper::toDTO);
    }

    public UsuarioResponseDTO buscarPorId(Integer id) {
        Usuario usuarioBuscado = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String emailUsuarioLogado = authentication.getName(); 
        
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        String emailDoUsuarioBuscado = usuarioBuscado.getEmail().getEndereco(); 

        if (!isAdmin && !emailDoUsuarioBuscado.equals(emailUsuarioLogado)) {
            throw new AccessDeniedException("Acesso negado: Você só pode visualizar o seu próprio perfil.");
        }

        return usuarioMapper.toDTO(usuarioBuscado);
    }

    @Transactional
    @CacheEvict(value = "usuarios", allEntries = true) // Limpa o cache ao deletar!
    public UsuarioResponseDTO atualizarUsuario(Integer id, UsuarioRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        
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
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}