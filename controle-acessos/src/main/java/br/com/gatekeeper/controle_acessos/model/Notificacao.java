package br.com.gatekeeper.controle_acessos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notificacao")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    @Column(length = 50)
    private String decisao;

    // Relacionamento com o parecer que vai receber a notificação
    @ManyToOne
    @JoinColumn(name = "parecer_id")
    private Parecer parecer;
}