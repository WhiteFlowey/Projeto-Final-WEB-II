package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.FAQ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqRepository extends JpaRepository<FAQ, Integer> {
    
    // Lista as perguntas e respostas (FAQ) referentes a um módulo específico
    List<FAQ> findByModuloId(Integer moduloId);
    
    // Pode procurar também por uma categoria específica (ex: "Erros Comuns")
    List<FAQ> findByCategoria(String categoria);
}