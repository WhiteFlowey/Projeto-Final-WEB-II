package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.dto.response.HistoricoAcessoResponseDTO;
import br.com.gatekeeper.controle_acessos.model.HistoricoAcesso;
import br.com.gatekeeper.controle_acessos.service.HistoricoAcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/historicos")
public class HistoricoAcessoController {

    @Autowired private HistoricoAcessoService service;

   @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistoricoAcessoResponseDTO>> buscarHistoricoDoUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.buscarHistoricoDoUsuario(usuarioId));
    }
}