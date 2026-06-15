package br.com.gatekeeper.controle_acessos.dto.response;

import org.springframework.hateoas.RepresentationModel;
import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false) // Necessário por causa da herança
public class SolicitacaoResponseDTO extends RepresentationModel<SolicitacaoResponseDTO> {

    private Integer id;
    private String protocolo;
    private String justificativa;
    private SolicitacaoStatus status;
    private LocalDateTime dataSolicitacao;
    private String nomeUsuario;
    private String nomeModulo;
}