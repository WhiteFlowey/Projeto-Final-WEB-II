package br.com.gatekeeper.controle_acessos.model;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "perfil")
public class Perfil implements GrantedAuthority {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 255)
    private String descricao;

    // --- MÉTODOS DO GRANTEDAUTHORITY ---
    
    @Override
    public String getAuthority() {
        // Retorna o nome do perfil para o Spring Security (ex: "ROLE_ADMIN")
        return this.nome;
    }
}