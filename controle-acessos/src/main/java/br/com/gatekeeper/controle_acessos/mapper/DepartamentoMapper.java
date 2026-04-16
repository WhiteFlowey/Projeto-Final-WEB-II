package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.DepartamentoDTO;
import br.com.gatekeeper.controle_acessos.model.Departamento;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    DepartamentoDTO toDTO(Departamento departamento);

    Departamento toEntity(DepartamentoDTO dto);
}