package br.com.gatekeeper.controle_acessos.model;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity // Indica que essa classe é uma entidade JPA (tabela no banco)
@Table(name = "perfil") 
public class Perfil implements GrantedAuthority {
    
    @Id
    // Chave primária gerada automaticamente pelo banco
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // Campo obrigatório com tamanho máximo de 50 caracteres
    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 255)
    // Descrição opcional do perfil
    private String descricao;

    // --- MÉTODOS DO GrantedAuthority ---
    
    @Override
    public String getAuthority() {
        // Retorna o nome do perfil para o Spring Security
        // Exemplo: "ROLE_ADMIN", "ROLE_COMUM"
        return this.nome;
    }
}