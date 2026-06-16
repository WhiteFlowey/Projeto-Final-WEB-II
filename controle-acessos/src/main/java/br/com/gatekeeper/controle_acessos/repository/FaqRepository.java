package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FAQ, Integer> {
    
    // Busca todos os FAQs de um módulo específico
    List<FAQ> findByModuloId(Integer moduloId);
    
    // Busca FAQs por categoria
    List<FAQ> findByCategoria(String categoria);
}