package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.model.ResponsavelModulo;
import br.com.gatekeeper.controle_acessos.service.ResponsavelModuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responsaveis")
public class ResponsavelModuloController {

    @Autowired
    private ResponsavelModuloService service;

    // Define um novo usuário como responsável por um módulo
    @PostMapping
    public ResponseEntity<ResponsavelModulo> definirResponsavel(@RequestBody ResponsavelModulo responsavel) {
        ResponsavelModulo novoResponsavel = service.definirResponsavel(responsavel);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoResponsavel);
    }

    // Lista quem são os responsáveis atuais (ou antigos) de um módulo específico
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<ResponsavelModulo>> listarPorModulo(@PathVariable Integer moduloId) {
        return ResponseEntity.ok(service.listarPorModulo(moduloId));
    }
}