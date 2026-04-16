package br.com.gatekeeper.controle_acessos.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ResponsavelModuloDTO {
    private Integer id;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    
    // Pegamos apenas os nomes para exibir na listagem
    private String nomeResponsavel;
    private String nomeModulo;
}