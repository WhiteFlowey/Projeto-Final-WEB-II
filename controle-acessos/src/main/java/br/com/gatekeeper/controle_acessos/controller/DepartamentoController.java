package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.model.Departamento;
import br.com.gatekeeper.controle_acessos.service.DepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    @Autowired private DepartamentoService service;

    @PostMapping
    public ResponseEntity<Departamento> criar(@RequestBody Departamento departamento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(departamento));
    }

    @GetMapping
    public ResponseEntity<List<Departamento>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}