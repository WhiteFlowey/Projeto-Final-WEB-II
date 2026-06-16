package br.com.gatekeeper.controle_acessos.dto.request;
import lombok.Data;

@Data // Gera getters e setters automaticamente
public class FaqRequestDTO {
    // Pergunta do FAQ
    private String pergunta;
    // Resposta do FAQ
    private String resposta;
}