package br.com.gatekeeper.controle_acessos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "perfil")
public class Perfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 255)
    private String descricao;
}