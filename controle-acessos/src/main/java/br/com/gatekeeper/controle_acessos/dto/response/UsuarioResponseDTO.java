package br.com.gatekeeper.controle_acessos.dto.response;

import br.com.gatekeeper.controle_acessos.model.enums.UsuarioStatus;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    
    private Integer id;
    private String nome;
    private String email;
    
    
    // Em vez de devolver o objeto inteiro de Departamento e Perfil, 
    // muitas vezes devolvemos apenas o nome para facilitar a exibição na tela
    private String nomeDepartamento;
    private String nomePerfil;
}