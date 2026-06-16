package br.com.gatekeeper.controle_acessos.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorDeErrosGlobais {

    // 1. Tratamento do 403 Forbidden (Perfis e IDOR)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroRespostaDTO> tratarAcessoNegado(AccessDeniedException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.FORBIDDEN.value(),
                "Acesso Negado",
                ex.getMessage(), //  Agora as nossas mensagens customizadas de segurança vão aparecer!
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    // 2. Tratando o erro 400 do @Valid (Junta todos os campos que falharam numa String só)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaDTO> tratarErrosDeValidacao(MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Erro de Validação",
                mensagem,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 3. Tratando busca de ID inexistente (404 Not Found)
    @ExceptionHandler(EntityNotFoundException.class) 
    public ResponseEntity<ErroRespostaDTO> tratarErro404(EntityNotFoundException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 4. Tratando Erro de Login Inválido (401 Unauthorized)
    @ExceptionHandler(BadCredentialsException.class) 
    public ResponseEntity<ErroRespostaDTO> tratarErroDeLogin(BadCredentialsException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.UNAUTHORIZED.value(),
                "Falha na Autenticação",
                "E-mail ou senha incorretos.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    // 5. O "Pega Tudo" para falhas graves do servidor (500 Internal Server Error)
    @ExceptionHandler(Exception.class) 
    public ResponseEntity<ErroRespostaDTO> tratarErro500(Exception ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno",
                "Ocorreu um erro inesperado no servidor. Contate o suporte.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroRespostaDTO> tratarErroRegraNegocio(IllegalArgumentException ex, HttpServletRequest request) {
        ErroRespostaDTO erro = new ErroRespostaDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Regra de Negócio Violada",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}