package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.model.enums.HistoricoAcessoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoAcessoRepository extends JpaRepository<HistoricoAcesso, Integer> {
    
    // Vê todo o histórico de acessos de um utilizador
    List<HistoricoAcesso> findByUsuarioId(Integer usuarioId);
    
    // Filtra históricos pelo seu estado (ex: encontrar todos os acessos atualmente ATIVOS)
    List<HistoricoAcesso> findByStatus(HistoricoAcessoStatus status);
}