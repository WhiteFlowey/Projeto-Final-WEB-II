package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.response.NotificacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Notificacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificacaoMapper {

    @Mapping(target = "protocoloSolicitacao", source = "solicitacao.protocolo")
    NotificacaoResponseDTO toDTO(Notificacao notificacao);
}