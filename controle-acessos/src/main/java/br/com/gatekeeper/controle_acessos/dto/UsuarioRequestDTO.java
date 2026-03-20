package br.com.gatekeeper.controle_acessos.dto;

import lombok.Data;

@Data
public class UsuarioRequestDTO {

    private String nome;
    
    private String email;
    
    // Recebemos a senha aqui para poder criptografar e salvar no banco
    private String senha;
    
    // O status pode vir no request (ex: o Admin já cria como ATIVO), 
    // ou podemos definir no Service que todo usuário novo nasce PENDENTE/INATIVO.
    // Vamos receber como String para facilitar o envio pelo front-end.
    private String status; 

    // Em vez de recebermos o objeto inteiro, recebemos apenas os IDs
    private Integer departamentoId;
    
    private Integer perfilId;
}