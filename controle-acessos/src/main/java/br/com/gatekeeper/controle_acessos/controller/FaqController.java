package br.com.gatekeeper.controle_acessos.controller;

import br.com.gatekeeper.controle_acessos.model.FAQ;
import br.com.gatekeeper.controle_acessos.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
public class FaqController {

    @Autowired
    private FaqService service;

    // Cria uma nova pergunta atrelada a um módulo específico
    @PostMapping("/modulo/{moduloId}")
    public ResponseEntity<FAQ> criarFaq(@RequestBody FAQ faq, @PathVariable Integer moduloId) {
        FAQ novoFaq = service.criarFaq(faq, moduloId);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoFaq);
    }

    // Lista todas as perguntas de um módulo para mostrar na tela de ajuda
    @GetMapping("/modulo/{moduloId}")
    public ResponseEntity<List<FAQ>> listarPorModulo(@PathVariable Integer moduloId) {
        return ResponseEntity.ok(service.listarPorModulo(moduloId));
    }
}