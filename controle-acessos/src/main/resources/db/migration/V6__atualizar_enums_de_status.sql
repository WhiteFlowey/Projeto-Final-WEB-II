-- 1. Atualiza os status aceitos no Histórico de Acesso
ALTER TABLE historico_acesso 
MODIFY COLUMN status ENUM('ATIVO', 'NEGADO', 'REVOGADO') NOT NULL;

-- 2. Blinda a tabela Parecer com as decisões exatas do Java
ALTER TABLE parecer 
MODIFY COLUMN decisao ENUM('APROVADA', 'RECUSADA') NOT NULL;

-- 3. Adiciona o status EM_ANALISE na Solicitação para alinhar com o Java
ALTER TABLE solicitacao 
MODIFY COLUMN status ENUM('PENDENTE', 'EM_ANALISE', 'APROVADA', 'REJEITADA') NOT NULL;