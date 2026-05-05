package br.com.gatekeeper.controle_acessos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;
    
    @NotBlank(message = "O e-mail não pode estar em branco")
    @Email(message = "O formato do e-mail é inválido")
    private String email;
    
    
    private String senha;
    
    // O status pode vir no request (ex: o Admin já cria como ATIVO), 
    // ou podemos definir no Service que todo usuário novo nasce PENDENTE/INATIVO.
   
    private String status; 

    // Em vez de recebermos o objeto inteiro, recebemos apenas os IDs
    private Integer departamentoId;
    
    private Integer perfilId;
    private String registroEmpregado;

    
}