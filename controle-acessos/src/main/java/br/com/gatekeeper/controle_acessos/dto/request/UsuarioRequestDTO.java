package br.com.gatekeeper.controle_acessos.dto.request;

import lombok.Data;

@Data
public class UsuarioRequestDTO {

    private String nome;
    
    private String email;
    
    
    private String senha;
    
    // O status pode vir no request (ex: o Admin já cria como ATIVO), 
    // ou podemos definir no Service que todo usuário novo nasce PENDENTE/INATIVO.
   
    private String status; 

    // Em vez de recebermos o objeto inteiro, recebemos apenas os IDs
    private Integer departamentoId;
    
    private Integer perfilId;
}