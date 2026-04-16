package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    
    // Converte a Entidade do Banco para o DTO de Resposta (Saída)
    UsuarioResponseDTO toDTO(Usuario usuario);
    
    // Converte o DTO de Requisição para a Entidade (Entrada)
    Usuario toEntity(UsuarioRequestDTO dto);
}