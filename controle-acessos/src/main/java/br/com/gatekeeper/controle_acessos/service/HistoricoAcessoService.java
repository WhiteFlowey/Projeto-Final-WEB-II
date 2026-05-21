package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.HistoricoAcessoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.HistoricoAcessoMapper;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.repository.HistoricoAcessoRepository;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; 
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistoricoAcessoService {

    @Autowired private HistoricoAcessoRepository repository;
    @Autowired private HistoricoAcessoMapper mapper;
    
    @Autowired private UsuarioRepository usuarioRepository; 
    @Autowired private ModuloRepository moduloRepository;

    public HistoricoAcessoResponseDTO registrarAcesso(HistoricoAcessoRequestDTO dto) {
        
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
                
        Modulo modulo = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado!"));

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