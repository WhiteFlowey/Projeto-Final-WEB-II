package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.DepartamentoDTO;
import br.com.gatekeeper.controle_acessos.model.Departamento;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring") // Permite que o Spring gerencie o Mapper
public interface DepartamentoMapper {
    // Converte uma entidade Departamento em DTO
    DepartamentoDTO toDTO(Departamento departamento);
    // Converte um DTO em entidade Departamento
    Departamento toEntity(DepartamentoDTO dto);
}