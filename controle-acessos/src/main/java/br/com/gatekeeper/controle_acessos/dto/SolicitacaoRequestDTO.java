package br.com.gatekeeper.controle_acessos.dto;

import lombok.Data;

@Data
public class SolicitacaoRequestDTO {

    // O que o utilizador digita na tela:
    private String justificativa;

    // O ID do módulo que ele quer aceder (selecionado num dropdown, por exemplo)
    private Integer moduloId;

    // O ID de quem está a pedir (geralmente pegamos isso do usuário logado, 
    // mas por agora recebemos na requisição)
    private Integer usuarioId;
    
    // NOTA: Não pedimos protocolo, status ou data. O sistema vai gerar isso sozinho!
}