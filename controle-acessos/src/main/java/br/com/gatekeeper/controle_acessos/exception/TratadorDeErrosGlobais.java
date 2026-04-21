package br.com.gatekeeper.controle_acessos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErrosGlobais {

    // 1. Tratando o famoso 403 Forbidden (Sem permissão)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroRespostaDTO> tratarAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                "Você não tem as permissões necessárias para realizar esta ação.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // 2. Tratando o erro 400 do @Valid (Quando esquecem de mandar um campo)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> tratarErrosDeValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Pega a mensagem específica que falhou (ex: "O email não pode ser vazio")
        String mensagem = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                mensagem,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 3. Tratando aquele erro de quando busca um ID e não acha (404 Not Found)
    @ExceptionHandler(RuntimeException.class) // Você pode criar uma excecao especifica como EntidadeNaoEncontradaException
    public ResponseEntity<ErroRespostaDTO> tratarErroGenerico(RuntimeException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                ex.getMessage(), // Aqui entra o seu "Histórico não encontrado"
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }
}