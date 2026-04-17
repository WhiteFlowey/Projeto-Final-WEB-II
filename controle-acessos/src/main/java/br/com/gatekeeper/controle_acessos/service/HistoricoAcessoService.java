package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.HistoricoAcessoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.HistoricoAcessoMapper;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.Usuario; // Importe o Usuario
import br.com.gatekeeper.controle_acessos.repository.HistoricoAcessoRepository;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository; // Importe
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository; // Importe
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoricoAcessoService {

    @Autowired private HistoricoAcessoRepository repository;
    @Autowired private HistoricoAcessoMapper mapper;
    
    // Adicionando os repositórios que faltavam para buscar o Pai e a Mãe
    @Autowired private UsuarioRepository usuarioRepository; 
    @Autowired private ModuloRepository moduloRepository;

    public HistoricoAcessoResponseDTO registrarAcesso(HistoricoAcessoRequestDTO dto) {
        
        // 1. Busca quem está acessando e onde está acessando
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado!"));
                
        Modulo modulo = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado!"));

        // 2. Converte o DTO e amarra todo mundo
        HistoricoAcesso historico = mapper.toEntity(dto);
        historico.setUsuario(usuario); // Amarra o usuário do banco
        historico.setModulo(modulo);   // Amarra o módulo do banco
        
        // 3. O servidor carimba a hora de entrada
        historico.setDataInicio(LocalDateTime.now());

        // 4. Salva e devolve
        historico = repository.save(historico);
        return mapper.toDTO(historico);
    }

    public List<HistoricoAcessoResponseDTO> buscarHistoricoDoUsuario(Integer usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(mapper::toDTO)
                .toList();
    }
}