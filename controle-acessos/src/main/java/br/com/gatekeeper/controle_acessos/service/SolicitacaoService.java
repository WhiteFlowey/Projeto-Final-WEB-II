package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.mapper.SolicitacaoMapper; // 1. Importar o mapper
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.Solicitacao;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.StatusSolicitacao;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.SolicitacaoRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SolicitacaoService {

    @Autowired private SolicitacaoRepository solicitacaoRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private ModuloRepository moduloRepository;
    
    // 2. Injetar o Mapper
    @Autowired private SolicitacaoMapper solicitacaoMapper;

    @Transactional
    public SolicitacaoResponseDTO criarSolicitacao(SolicitacaoRequestDTO request) {
        
        // Validações continuam aqui (é regra de negócio!)
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado. ID: " + request.getUsuarioId()));

        Modulo modulo = moduloRepository.findById(request.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado. ID: " + request.getModuloId()));

        // 3. O Mapper cria a entidade (ele ignora usuario e modulo conforme configuramos)
        Solicitacao solicitacao = solicitacaoMapper.toEntity(request);

        // 4. Regras Automáticas continuam aqui
        solicitacao.setUsuario(usuario);
        solicitacao.setModulo(modulo);
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        solicitacao.setProtocolo(gerarProtocolo());

        solicitacao = solicitacaoRepository.save(solicitacao);

        // 5. Retorno direto pelo Mapper (Adeus converterParaResponseDTO!)
        return solicitacaoMapper.toDTO(solicitacao);
    }

    private String gerarProtocolo() {
        int ano = LocalDateTime.now().getYear();
        String codigoAleatorio = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "REQ-" + ano + "-" + codigoAleatorio;
    }
    
  
}