package br.com.gatekeeper.controle_acessos.dto.request;

import br.com.gatekeeper.controle_acessos.model.enums.HistoricoAcessoStatus;
import lombok.Data;

@Data
public class HistoricoAcessoRequestDTO {
    private Integer usuarioId;
    private Integer moduloId;
    private HistoricoAcessoStatus status;
}