package br.com.gatekeeper.controle_acessos.model;

import br.com.gatekeeper.controle_acessos.model.enums.StatusUsuario;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String senha;

    // O EnumType.STRING garante que salve o texto ("ATIVO") e não o número da posição
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuario status;

    // Relacionamento com a tabela de Departamento
    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    // Relacionamento com a tabela de Perfil
    @ManyToOne
    @JoinColumn(name = "perfil_id", nullable = false)
    private Perfil perfil;
}