package br.com.gatekeeper.controle_acessos.exception;
import java.time.LocalDateTime;
import lombok.Data;

@Data 
public class ErroRespostaDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private String erro;
    private String mensagem;
    private String caminho;

    public ErroRespostaDTO(Integer status, String erro, String mensagem, String caminho) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.caminho = caminho;
    }
}