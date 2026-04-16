package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HistoricoAcessoMapper {

    @Mapping(target = "nomeModulo", source = "modulo.nome")
    HistoricoAcessoResponseDTO toDTO(HistoricoAcesso historico);
}