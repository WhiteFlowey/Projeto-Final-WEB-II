package br.com.gatekeeper.controle_acessos.dto.response;

import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SolicitacaoResponseDTO {

    private Integer id;
    private String protocolo;
    private String justificativa;
    private SolicitacaoStatus status;
    private LocalDateTime dataSolicitacao;
    
    private String nomeUsuario;
    private String nomeModulo;
}