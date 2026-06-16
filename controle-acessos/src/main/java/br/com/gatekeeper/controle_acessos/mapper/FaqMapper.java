package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.request.FaqRequestDTO;
import br.com.gatekeeper.controle_acessos.dto.response.FaqResponseDTO;
import br.com.gatekeeper.controle_acessos.model.FAQ;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FaqMapper {
    // Converte Entidade para DTO de resposta
    FaqResponseDTO toDTO(FAQ faq);
    // Converte DTO recebido em Entidade
    FAQ toEntity(FaqRequestDTO dto);
}