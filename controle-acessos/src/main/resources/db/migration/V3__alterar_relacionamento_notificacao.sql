-- 1. Remove a ligação com a Solicitação
ALTER TABLE notificacao DROP FOREIGN KEY fk_notif_solicitacao;
ALTER TABLE notificacao DROP COLUMN solicitacao_id;

-- 2. Remove a ligação com o Usuário
ALTER TABLE notificacao DROP FOREIGN KEY fk_notif_usuario;
ALTER TABLE notificacao DROP COLUMN usuario_id;

-- 3. Cria a nova ligação com o Parecer
ALTER TABLE notificacao ADD COLUMN parecer_id INT;
ALTER TABLE notificacao ADD CONSTRAINT fk_notificacao_parecer FOREIGN KEY (parecer_id) REFERENCES parecer(id);