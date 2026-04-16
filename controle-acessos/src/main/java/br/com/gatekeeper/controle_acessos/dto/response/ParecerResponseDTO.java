package br.com.gatekeeper.controle_acessos.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ParecerResponseDTO {
    private Integer id;
    private String descricao;
    private String decisao;
    private LocalDateTime dataParecer;
    private String nomeAvaliador;
}