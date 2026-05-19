package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Ensinamos o Spring a olhar "dentro" do Value Object (email.endereco) para fazer a busca
    @Query("SELECT u FROM Usuario u WHERE u.email.endereco = :email")
    UserDetails findByEmail(String email);

}