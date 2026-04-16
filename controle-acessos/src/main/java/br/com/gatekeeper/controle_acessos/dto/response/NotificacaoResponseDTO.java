package br.com.gatekeeper.controle_acessos.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificacaoResponseDTO {
    private Integer id;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private String decisao;
    private Integer parecerId; 
    private String protocoloSolicitacao;
}