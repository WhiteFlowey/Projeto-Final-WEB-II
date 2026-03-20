package br.com.gatekeeper.controle_acessos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "departamento")
public class Departamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;
}