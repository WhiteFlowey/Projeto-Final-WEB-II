package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Solicitacao;
import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Integer> {

    // Equivale ao seu listarSolicitacoesPorUsuario()
    List<Solicitacao> findByUsuarioId(Integer usuarioId);

    // Busca uma solicitação específica pelo número do protocolo gerado
    Solicitacao findByProtocolo(String protocolo);
    
    // Lista as solicitações filtrando pelo status (ex: buscar todas as PENDENTES)
    List<Solicitacao> findByStatus(SolicitacaoStatus status);
}