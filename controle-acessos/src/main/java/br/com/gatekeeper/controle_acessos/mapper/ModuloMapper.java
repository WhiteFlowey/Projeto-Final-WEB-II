package br.com.gatekeeper.controle_acessos.mapper;

import br.com.gatekeeper.controle_acessos.dto.ModuloDTO;
import br.com.gatekeeper.controle_acessos.model.Modulo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ModuloMapper {
    
    ModuloDTO toDTO(Modulo modulo);
    
    Modulo toEntity(ModuloDTO dto);
}