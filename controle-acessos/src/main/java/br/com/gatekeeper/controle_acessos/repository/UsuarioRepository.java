package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    // O JpaRepository já traz de graça: save(), findAll(), findById(), deleteById()
    
    // Se precisarmos buscar um usuário pelo email na hora do login, o Spring 
    // cria o comando SQL automaticamente só lendo o nome deste método:
    Usuario findByEmail(String email);
}