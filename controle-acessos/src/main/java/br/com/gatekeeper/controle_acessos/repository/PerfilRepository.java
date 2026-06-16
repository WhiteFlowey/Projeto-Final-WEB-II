package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository // Indica que essa interface é um componente de acesso ao banco de dados
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
}
// JpaRepository fornece métodos CRUD prontos como salvar, buscar, listar, deletar e contar registros automaticamente. O primeiro parâmetro é a entidade (Perfil) e o segundo é o tipo da chave primária (Integer).