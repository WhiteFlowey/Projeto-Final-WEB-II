package br.com.gatekeeper.controle_acessos.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.gatekeeper.controle_acessos.dto.request.HistoricoAcessoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.HistoricoAcessoMapper;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.repository.HistoricoAcessoRepository;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class HistoricoAcessoService {

    private final HistoricoAcessoRepository repository;
    private final HistoricoAcessoMapper mapper;
    
    private final UsuarioRepository usuarioRepository; 
    private final ModuloRepository moduloRepository;

    HistoricoAcessoService(HistoricoAcessoMapper mapper, UsuarioRepository usuarioRepository, ModuloRepository moduloRepository, HistoricoAcessoRepository repository) {
        this.mapper = mapper;
        this.usuarioRepository = usuarioRepository;
        this.moduloRepository = moduloRepository;
        this.repository = repository;
    }

    public HistoricoAcessoResponseDTO registrarAcesso(HistoricoAcessoRequestDTO dto) {
        
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
                
        Modulo modulo = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new EntityNotFoundException("Módulo não encontrado!"));

        HistoricoAcesso historico = mapper.toEntity(dto);
        historico.setUsuario(usuario); 
        historico.setModulo(modulo);   
        
        historico.setDataInicio(LocalDateTime.now());

        historico = repository.save(historico);
        return mapper.toDTO(historico);
    }

    // ➕ NOVO: Método que lista absolutamente tudo no sistema (com paginação)
    public Page<HistoricoAcessoResponseDTO> listarHistoricoPaginado(Pageable paginacao) {
        return repository.findAll(paginacao).map(mapper::toDTO);
    }

    // 🔄 ATUALIZADO: Método que busca o histórico de um usuário específico agora fatiado por páginas
    public Page<HistoricoAcessoResponseDTO> buscarHistoricoDoUsuario(Integer usuarioId, Pageable paginacao) {
        return repository.findByUsuarioId(usuarioId, paginacao).map(mapper::toDTO);
    }
}