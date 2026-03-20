package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Parecer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParecerRepository extends JpaRepository<Parecer, Integer> {
    
    // Retorna todos os pareceres dados a uma solicitação específica
    List<Parecer> findBySolicitacaoId(Integer solicitacaoId);
}