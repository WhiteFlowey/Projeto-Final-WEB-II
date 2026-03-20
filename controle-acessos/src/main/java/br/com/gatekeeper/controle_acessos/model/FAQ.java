package br.com.gatekeeper.controle_acessos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "faq")
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String pergunta;

    // Usamos TEXT porque a resposta do FAQ pode ser bem longa
    @Column(nullable = false, columnDefinition = "TEXT")
    private String resposta;

    @Column(length = 100)
    private String categoria;

    // Relacionamento com o Módulo ao qual este FAQ pertence
    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
}