package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que é um componente de acesso ao banco
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
}
// JpaRepository já fornece métodos prontos