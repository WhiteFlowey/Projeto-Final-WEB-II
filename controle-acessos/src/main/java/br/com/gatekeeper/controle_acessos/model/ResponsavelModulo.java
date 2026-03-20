package br.com.gatekeeper.controle_acessos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "responsavel_modulo")
public class ResponsavelModulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    // dataFim pode ser nula, pois a pessoa pode ser responsável por tempo indeterminado
    @Column(name = "data_fim") 
    private LocalDateTime dataFim;

    // Relacionamento com o Usuário que é o responsável
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacionamento com o Módulo pelo qual ele é responsável
    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
}