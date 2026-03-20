package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.model.Modulo;
import br.com.gatekeeper.controle_acessos.service.ModuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/modulos")
public class ModuloController {

    @Autowired private ModuloService service;

    @PostMapping
    public ResponseEntity<Modulo> criar(@RequestBody Modulo modulo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(modulo));
    }

    @GetMapping
    public ResponseEntity<List<Modulo>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}