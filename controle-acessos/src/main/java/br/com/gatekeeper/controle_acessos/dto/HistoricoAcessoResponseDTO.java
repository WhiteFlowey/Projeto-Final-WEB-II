package br.com.gatekeeper.controle_acessos.dto;

import br.com.gatekeeper.controle_acessos.model.enums.StatusHistoricoAcesso;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HistoricoAcessoResponseDTO {
    private Integer id;
    private String nomeModulo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private StatusHistoricoAcesso status;
}