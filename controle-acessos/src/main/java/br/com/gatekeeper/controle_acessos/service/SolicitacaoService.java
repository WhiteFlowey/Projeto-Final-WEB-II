package br.com.gatekeeper.controle_acessos.service;

import br.com.gatekeeper.controle_acessos.dto.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.model.Solicitacao;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.enums.StatusSolicitacao;
import br.com.gatekeeper.controle_acessos.repository.ModuloRepository;
import br.com.gatekeeper.controle_acessos.repository.SolicitacaoRepository;
import br.com.gatekeeper.controle_acessos.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SolicitacaoService {

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    public SolicitacaoResponseDTO criarSolicitacao(SolicitacaoRequestDTO request) {
        
        // 1. Validamos se o Usuário e o Módulo realmente existem no banco
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado. ID: " + request.getUsuarioId()));

        Modulo modulo = moduloRepository.findById(request.getModuloId())
                .orElseThrow(() -> new RuntimeException("Módulo não encontrado. ID: " + request.getModuloId()));

        // 2. Criamos a nova entidade
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.setJustificativa(request.getJustificativa());
        solicitacao.setUsuario(usuario);
        solicitacao.setModulo(modulo);

        // ==========================================================
        // 3. REGRAS DE NEGÓCIO AUTOMÁTICAS (A "Inteligência")
        // ==========================================================
        
        // A data é o exato momento em que o código roda
        solicitacao.setDataSolicitacao(LocalDateTime.now());
        
        // Toda nova solicitação nasce obrigatoriamente como PENDENTE
        solicitacao.setStatus(StatusSolicitacao.PENDENTE);
        
        // Geramos um protocolo único para o utilizador poder acompanhar depois
        solicitacao.setProtocolo(gerarProtocolo());

        // 4. Salvamos a solicitação blindada no banco de dados
        solicitacao = solicitacaoRepository.save(solicitacao);

        // 5. Devolvemos o DTO de resposta para mostrar na tela
        return converterParaResponseDTO(solicitacao);
    }

    // Método privado para gerar o protocolo no formato REQ-ANO-CÓDIGO (Ex: REQ-2026-A1B2C3)
    private String gerarProtocolo() {
        int ano = LocalDateTime.now().getYear();
        // Usamos o UUID do Java para gerar uma sequência aleatória e pegamos os primeiros 6 caracteres
        String codigoAleatorio = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "REQ-" + ano + "-" + codigoAleatorio;
    }

    // Converte a entidade guardada no banco para o DTO de Saída
    private SolicitacaoResponseDTO converterParaResponseDTO(Solicitacao solicitacao) {
        SolicitacaoResponseDTO dto = new SolicitacaoResponseDTO();
        dto.setId(solicitacao.getId());
        dto.setProtocolo(solicitacao.getProtocolo());
        dto.setJustificativa(solicitacao.getJustificativa());
        dto.setStatus(solicitacao.getStatus());
        dto.setDataSolicitacao(solicitacao.getDataSolicitacao());
        
        // Pegamos apenas os nomes para a tela ficar limpa
        dto.setNomeUsuario(solicitacao.getUsuario().getNome());
        dto.setNomeModulo(solicitacao.getModulo().getNome());
        
        return dto;
    }
}