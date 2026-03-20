package br.com.gatekeeper.controle_acessos.model;

import br.com.gatekeeper.controle_acessos.model.enums.StatusHistoricoAcesso;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "historico_acesso")
public class HistoricoAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    // dataFim pode ser nula inicialmente, pois o acesso pode estar ATIVO e ainda não ter terminado
    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    // Mapeamento do Enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusHistoricoAcesso status;

    // Relacionamento com o Usuário que obteve o acesso
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacionamento com o Módulo que foi acedido
    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
}