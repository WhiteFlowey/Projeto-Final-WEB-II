package br.com.gatekeeper.controle_acessos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitacaoRequestDTO {

    // justificativa
    private String justificativa;

    // O ID do módulo que ele quer aceder 
    private Integer moduloId;

    // O ID de quem está a pedir 
    private Integer usuarioId;

    @NotNull(message = "A quantidade de dias é obrigatória")
    @Min(value = 1, message = "A quantidade de dias deve ser pelo menos 1")
    private Integer qtdDias;
    
    // NOTA: Não pedimos protocolo, status ou data. O sistema vai gerar isso sozinho!
}