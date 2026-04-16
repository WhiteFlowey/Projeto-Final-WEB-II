package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.request.SolicitacaoRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.SolicitacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Solicitacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitacaoMapper {

    @Mapping(target = "nomeUsuario", source = "usuario.nome")
    @Mapping(target = "nomeModulo", source = "modulo.nome")
    SolicitacaoResponseDTO toDTO(Solicitacao solicitacao);

    // No mapeamento para Entidade, ignoramos os campos que o Service buscará no banco
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "modulo", ignore = true)
    Solicitacao toEntity(SolicitacaoRequestDTO dto);
}