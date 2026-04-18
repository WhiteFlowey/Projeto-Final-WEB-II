package br.com.gatekeeper.controle_acessos.dto.request;

import lombok.Data;

@Data
public class ParecerRequestDTO {
    private String descricao;
    private String decisao; 
    private Integer solicitacaoId;
    private Integer usuarioResponsavelId;
}