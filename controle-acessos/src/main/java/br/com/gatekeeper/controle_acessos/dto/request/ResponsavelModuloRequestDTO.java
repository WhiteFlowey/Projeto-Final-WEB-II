package br.com.gatekeeper.controle_acessos.dto.request;

import lombok.Data;

@Data
public class ResponsavelModuloRequestDTO {

    private Integer usuarioId;
    private Integer moduloId;
}