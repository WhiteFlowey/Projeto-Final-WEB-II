package br.com.gatekeeper.controle_acessos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitacaoRequestDTO {

    private String justificativa;

    private Integer moduloId;

    private Integer usuarioId;

    @NotNull(message = "A quantidade de dias é obrigatória")
    @Min(value = 1, message = "A quantidade de dias deve ser pelo menos 1")
    private Integer qtdDias;
    
}