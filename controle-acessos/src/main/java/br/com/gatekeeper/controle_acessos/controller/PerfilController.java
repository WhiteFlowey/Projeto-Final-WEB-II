package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.model.Perfil;
import br.com.gatekeeper.controle_acessos.service.PerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/perfis")
public class PerfilController {

    @Autowired private PerfilService service;

    @PostMapping
    public ResponseEntity<Perfil> criar(@RequestBody Perfil perfil) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(perfil));
    }

    @GetMapping
    public ResponseEntity<List<Perfil>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}