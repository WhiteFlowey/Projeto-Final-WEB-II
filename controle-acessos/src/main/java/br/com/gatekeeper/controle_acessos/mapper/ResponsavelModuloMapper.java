package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.ResponsavelModuloDTO;
import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResponsavelModuloMapper {

    @Mapping(target = "nomeResponsavel", source = "usuario.nome")
    @Mapping(target = "nomeModulo", source = "modulo.nome")
    ResponsavelModuloDTO toDTO(ResponsavelModulo responsavel);

    // Se precisar criar um mapeamento de volta para a entidade
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "modulo", ignore = true)
    ResponsavelModulo toEntity(ResponsavelModuloDTO dto);
}