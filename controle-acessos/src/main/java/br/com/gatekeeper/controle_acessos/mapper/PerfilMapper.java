package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PerfilMapper {
    
    PerfilDTO toDTO(Perfil perfil);
    
    Perfil toEntity(PerfilDTO dto);
}