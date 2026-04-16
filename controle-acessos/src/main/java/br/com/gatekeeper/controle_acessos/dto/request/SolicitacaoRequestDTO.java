package br.com.gatekeeper.controle_acessos.dto.request;

import lombok.Data;

@Data
public class SolicitacaoRequestDTO {

    // justificativa
    private String justificativa;

    // O ID do módulo que ele quer aceder 
    private Integer moduloId;

    // O ID de quem está a pedir 
    private Integer usuarioId;
    
    // NOTA: Não pedimos protocolo, status ou data. O sistema vai gerar isso sozinho!
}