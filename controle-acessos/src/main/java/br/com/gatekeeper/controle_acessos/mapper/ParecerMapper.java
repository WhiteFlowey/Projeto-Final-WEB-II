package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.request.ParecerRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.ParecerResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Parecer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParecerMapper {

    @Mapping(target = "nomeAvaliador", source = "usuarioResponsavel.nome")
    ParecerResponseDTO toDTO(Parecer parecer);

    @Mapping(target = "solicitacao", ignore = true)
    @Mapping(target = "usuarioResponsavel", ignore = true)
    Parecer toEntity(ParecerRequestDTO dto);
}