package br.com.gatekeeper.controle_acessos.dto.request;
import lombok.Data;

@Data
public class FaqRequestDTO {
    private String pergunta;
    private String resposta;
}