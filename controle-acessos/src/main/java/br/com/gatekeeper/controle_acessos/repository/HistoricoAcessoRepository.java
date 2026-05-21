package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.enums.HistoricoAcessoStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HistoricoAcessoRepository extends JpaRepository<HistoricoAcesso, Integer> {
    
    // Vê todo o histórico de acessos de um utilizador
    Page<HistoricoAcesso> findByUsuarioId(Integer usuarioId, Pageable pageable);
    
    // Filtra históricos pelo seu estado (ex: encontrar todos os acessos atualmente ATIVOS)
    Page<HistoricoAcesso> findByStatus(HistoricoAcessoStatus status, Pageable pageable);
}