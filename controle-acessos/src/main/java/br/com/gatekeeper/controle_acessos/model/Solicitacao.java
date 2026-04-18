package br.com.gatekeeper.controle_acessos.model;

import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "solicitacao")
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String protocolo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String justificativa;

    // Mapeamento do Enum para gravar o texto no banco
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SolicitacaoStatus status;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;

    // Relacionamento com o Usuário que fez o pedido
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relacionamento com o Módulo que ele quer aceder
    @ManyToOne
    @JoinColumn(name = "modulo_id", nullable = false)
    private Modulo modulo;
}