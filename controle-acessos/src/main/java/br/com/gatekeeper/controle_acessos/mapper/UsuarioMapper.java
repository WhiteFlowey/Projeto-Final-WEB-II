package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.request.UsuarioRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.UsuarioResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Usuario;
import br.com.gatekeeper.controle_acessos.model.vo.Email; // <-- Import do VO
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(source = "departamento.nome", target = "nomeDepartamento")
    @Mapping(source = "perfil.nome", target = "nomePerfil")
    @Mapping(source = "email.endereco", target = "email") 
    UsuarioResponseDTO toDTO(Usuario usuario);

    // Silenciando avisos de campos que não vem na criação
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departamento", ignore = true)
    @Mapping(target = "perfil", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    // MapStruct a converter String para Email VO sozinho!
    default Email stringToEmail(String endereco) {
        return endereco != null ? new Email(endereco) : null;
    }
}