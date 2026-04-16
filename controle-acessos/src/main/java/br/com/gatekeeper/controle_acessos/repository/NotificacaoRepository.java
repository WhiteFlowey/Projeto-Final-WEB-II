package br.com.gatekeeper.controle_acessos.repository;

import br.com.gatekeeper.controle_acessos.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {

    // A mágica acontece aqui: Navegamos da Notificacao -> Parecer -> Solicitacao -> Usuario
    @Query("SELECT n FROM Notificacao n WHERE n.parecer.solicitacao.usuario.id = :usuarioId ORDER BY n.dataEnvio DESC") 
    // ORDER BY n.dataEnvio DESC garante que as notificações mais recentes apareçam primeiro
    List<Notificacao> buscarTodasDoUsuario(@Param("usuarioId") Integer usuarioId);
    
}