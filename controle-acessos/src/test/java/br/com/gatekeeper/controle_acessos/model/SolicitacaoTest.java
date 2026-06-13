package br.com.gatekeeper.controle_acessos.model;

import br.com.gatekeeper.controle_acessos.model.enums.SolicitacaoStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

// Importação estática do AssertJ para deixar o código mais legível
import static org.assertj.core.api.Assertions.assertThat;

class SolicitacaoTest {

    @Test
    void deveAtribuirERecuperarAtributosBasicosCorretamente() {

        // ARRANGE (Preparar)
        // Instanciamos o objeto que será testado e preparamos os dados falsos
        Solicitacao solicitacao = new Solicitacao();
        LocalDateTime dataAtual = LocalDateTime.now();
        String protocoloEsperado = "REQ-2026-XYZ123";
        String justificativaEsperada = "Gostaria de acessar o módulo financeiro para gerar relatórios.";

        // ACT (Agir)
        // Usamos os setters gerados pelo Lombok para preencher o objeto
        solicitacao.setId(1);
        solicitacao.setProtocolo(protocoloEsperado);
        solicitacao.setJustificativa(justificativaEsperada);
        solicitacao.setQtdDias(15);
        solicitacao.setStatus(SolicitacaoStatus.PENDENTE);
        solicitacao.setDataSolicitacao(dataAtual);

        // ASSERT (Verificar)
        // Verificamos se o que colocamos lá dentro é exatamente o que sai
        assertThat(solicitacao.getId()).isEqualTo(1);
        assertThat(solicitacao.getProtocolo()).isEqualTo(protocoloEsperado);
        assertThat(solicitacao.getJustificativa()).isEqualTo(justificativaEsperada);
        assertThat(solicitacao.getQtdDias()).isEqualTo(15);
        assertThat(solicitacao.getStatus()).isEqualTo(SolicitacaoStatus.PENDENTE);
        assertThat(solicitacao.getDataSolicitacao()).isEqualTo(dataAtual);
    }

    @Test
    void deveAssociarUsuarioEModuloCorretamente() {

        // ARRANGE (Preparar)
        Solicitacao solicitacao = new Solicitacao();
        
        // Criamos instâncias vazias de Usuario e Modulo apenas com o ID para simular o relacionamento
        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(99);
        
        Modulo moduloMock = new Modulo();
        moduloMock.setId(5);

        // ACT (Agir)
        // Faz a associação na nossa Solicitação
        solicitacao.setUsuario(usuarioMock);
        solicitacao.setModulo(moduloMock);

        // ASSERT (Verificar)
        // Garante que os objetos não são nulos e que os IDs correspondem
        assertThat(solicitacao.getUsuario()).isNotNull();
        assertThat(solicitacao.getUsuario().getId()).isEqualTo(99);
        
        assertThat(solicitacao.getModulo()).isNotNull();
        assertThat(solicitacao.getModulo().getId()).isEqualTo(5);
    }
}