package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.response.NotificacaoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.Notificacao;
import br.com.gatekeeper.controle_acessos.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {

    @Autowired 
    private NotificacaoService service;

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacaoResponseDTO>> buscarPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarNotificacoesDoUsuario(usuarioId));
    }
}