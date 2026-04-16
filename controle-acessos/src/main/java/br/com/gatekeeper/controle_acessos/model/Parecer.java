package br.com.gatekeeper.controle_acessos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List; // <--- A LINHA QUE FALTAVA PARA RESOLVER O ERRO!

@Data
@Entity
@Table(name = "parecer")
public class Parecer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_parecer", nullable = false)
    private LocalDateTime dataParecer;

    @Column(nullable = false, length = 50)
    private String decisao;

    // Relacionamento com a Solicitação que está a ser avaliada
    @ManyToOne
    @JoinColumn(name = "solicitacao_id", nullable = false)
    private Solicitacao solicitacao;

    // Relacionamento com o Usuário (Avaliador/Gestor) que deu o parecer
    @ManyToOne
    @JoinColumn(name = "usuario_responsavel_id", nullable = false)
    private Usuario usuarioResponsavel;

    @OneToMany(mappedBy = "parecer")
    private List<Notificacao> notificacoes; // Agora o Java sabe o que é isso!
}