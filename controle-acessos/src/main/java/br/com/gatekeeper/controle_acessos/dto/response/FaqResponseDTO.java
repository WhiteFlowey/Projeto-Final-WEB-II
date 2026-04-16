package br.com.gatekeeper.controle_acessos.dto.response;

public record FaqResponseDTO(
    Long id,
    String pergunta,
    String resposta,
    String categoria
) {}