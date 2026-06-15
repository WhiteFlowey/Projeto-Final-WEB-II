package br.com.gatekeeper.controle_acessos.dto;

import lombok.Data; // Lombok gera automaticamente getters, setters, toString, equals e hashCode

@Data
public class PerfilDTO {
    // Identificador único do perfil
    private Integer id;
    // Nome do perfil (ex: ADMIN, COMUM, GESTOR)
    private String nome;
    // Descrição do perfil (explica a função dele no sistema)
    private String descricao;
}