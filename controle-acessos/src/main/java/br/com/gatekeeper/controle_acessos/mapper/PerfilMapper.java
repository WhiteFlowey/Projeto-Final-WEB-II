package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.PerfilDTO;
import br.com.gatekeeper.controle_acessos.model.Perfil;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
// Indica que o MapStruct vai gerar a implementação automaticamente
// e o Spring vai gerenciar como um Bean (injeção de dependência)
public interface PerfilMapper {
     // Converte a entidade Perfil (banco) para PerfilDTO (API)
    PerfilDTO toDTO(Perfil perfil);
     // Converte PerfilDTO (API) para entidade Perfil (banco)
    Perfil toEntity(PerfilDTO dto);
}