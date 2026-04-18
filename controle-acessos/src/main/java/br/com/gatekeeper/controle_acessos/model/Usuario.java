package br.com.gatekeeper.controle_acessos.model;

import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UsuarioStatus status;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    // =========================================================================
    // MÉTODOS DO CONTRATO USERDETAILS (SPRING SECURITY)
    // =========================================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devolve uma lista contendo o perfil do usuário
        return List.of(this.perfil);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        // Informamos ao Spring que o login será feito usando o e-mail
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta não bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciais não expiram
    }

    @Override
    public boolean isEnabled() {
        // O usuário só consegue logar se o status dele for ATIVO
        return this.status == UsuarioStatus.ATIVO;
    }
}