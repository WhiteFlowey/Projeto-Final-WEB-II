package br.com.gatekeeper.controle_acessos.model;

import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import br.com.gatekeeper.controle_acessos.model.vo.Email; 
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

    @Embedded
    private Email email;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(name = "registro_empregado")
    private String registroEmpregado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UsuarioStatus status;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;

    // MÉTODOS DO CONTRATO USERDETAILS (SPRING SECURITY)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(this.perfil);
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email.getEndereco(); // Pega a String de dentro do VO
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status == UsuarioStatus.ATIVO;
    }
}