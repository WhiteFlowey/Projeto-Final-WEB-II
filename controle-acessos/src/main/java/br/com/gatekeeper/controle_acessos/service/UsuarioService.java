package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Departamento;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.StatusUsuario;
import br.com.gatekeeper.controle_acessos.repository.DepartamentoRepository;
import br.com.gatekeeper.controle_acessos.repository.PerfilRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    // O Spring injeta automaticamente os repositórios que criamos
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    public UsuarioResponseDTO criarUsuario(UsuarioRequestDTO request) {
        // 1. Buscamos as entidades reais no banco de dados usando os IDs que vieram do front-end
        Departamento departamento = departamentoRepository.findById(request.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        Perfil perfil = perfilRepository.findById(request.getPerfilId())
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado"));

        // 2. Montamos a entidade Usuario que será salva no banco (Model)
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(request.getSenha()); // Num sistema real, aqui usaríamos um BCrypt para criptografar
        usuario.setStatus(StatusUsuario.valueOf(request.getStatus().toUpperCase()));
        usuario.setDepartamento(departamento);
        usuario.setPerfil(perfil);

        // 3. Salvamos no banco de dados
        usuario = usuarioRepository.save(usuario);

        // 4. Convertamos a entidade salva para o DTO de Resposta (escondendo a senha)
        return converterParaResponseDTO(usuario);
    }

    // Método auxiliar para não repetirmos código de conversão
    private UsuarioResponseDTO converterParaResponseDTO(Usuario usuario) {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setStatus(usuario.getStatus());
        response.setNomeDepartamento(usuario.getDepartamento().getNome());
        response.setNomePerfil(usuario.getPerfil().getNome());
        return response;
    }

    public List<UsuarioResponseDTO> listarTodos() {
        // Vai no banco, busca todos os usuários e transforma cada um deles no DTO sem a senha
        return usuarioRepository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }
}