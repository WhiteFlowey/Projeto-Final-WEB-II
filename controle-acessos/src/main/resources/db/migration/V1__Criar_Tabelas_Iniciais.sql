-- =================================================================================
-- 1. TABELAS INDEPENDENTES (Não têm chaves estrangeiras)
-- =================================================================================

CREATE TABLE departamento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE perfil (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    descricao VARCHAR(255)
);

CREATE TABLE modulo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(255)
);

-- =================================================================================
-- 2. TABELA USUÁRIO (Depende de Departamento e Perfil)
-- =================================================================================

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL, -- Mantido como VARCHAR(20) conforme o seu diagrama
    departamento_id INT NOT NULL,
    perfil_id INT NOT NULL,
    
    CONSTRAINT fk_usuario_depto FOREIGN KEY (departamento_id) REFERENCES departamento(id),
    CONSTRAINT fk_usuario_perfil FOREIGN KEY (perfil_id) REFERENCES perfil(id)
);

-- =================================================================================
-- 3. TABELAS INTERMÉDIAS (Dependem de Usuário e/ou Módulo)
-- =================================================================================

CREATE TABLE solicitacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    protocolo VARCHAR(50) NOT NULL UNIQUE,
    justificativa TEXT NOT NULL,
    status ENUM('PENDENTE', 'APROVADA', 'REJEITADA') NOT NULL, -- Blindado com ENUM
    data_solicitacao DATETIME NOT NULL,
    usuario_id INT NOT NULL,
    modulo_id INT NOT NULL,
    
    CONSTRAINT fk_solicitacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_solicitacao_modulo FOREIGN KEY (modulo_id) REFERENCES modulo(id)
);

CREATE TABLE faq (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pergunta VARCHAR(255) NOT NULL,
    resposta TEXT NOT NULL,
    categoria VARCHAR(100),
    modulo_id INT NOT NULL,
    
    CONSTRAINT fk_faq_modulo FOREIGN KEY (modulo_id) REFERENCES modulo(id)
);

CREATE TABLE responsavel_modulo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME,
    usuario_id INT NOT NULL,
    modulo_id INT NOT NULL,
    
    CONSTRAINT fk_resp_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_resp_modulo FOREIGN KEY (modulo_id) REFERENCES modulo(id)
);

CREATE TABLE historico_acesso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_inicio DATETIME NOT NULL,
    data_fim DATETIME,
    status ENUM('ATIVO', 'ENCERRADO', 'REVOGADO') NOT NULL, -- Blindado com ENUM
    usuario_id INT NOT NULL,
    modulo_id INT NOT NULL,
    
    CONSTRAINT fk_hist_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_hist_modulo FOREIGN KEY (modulo_id) REFERENCES modulo(id)
);

-- =================================================================================
-- 4. TABELAS FINAIS (Dependem da Solicitação)
-- =================================================================================

CREATE TABLE notificacao (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mensagem TEXT NOT NULL,
    data_envio DATETIME NOT NULL,
    decisao VARCHAR(50),
    solicitacao_id INT NOT NULL,
    usuario_id INT NOT NULL,
    
    CONSTRAINT fk_notif_solicitacao FOREIGN KEY (solicitacao_id) REFERENCES solicitacao(id),
    CONSTRAINT fk_notif_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE parecer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    descricao TEXT NOT NULL,
    data_parecer DATETIME NOT NULL,
    decisao VARCHAR(50) NOT NULL,
    solicitacao_id INT NOT NULL,
    usuario_responsavel_id INT NOT NULL,
    
    CONSTRAINT fk_parecer_solicitacao FOREIGN KEY (solicitacao_id) REFERENCES solicitacao(id),
    CONSTRAINT fk_parecer_usuario FOREIGN KEY (usuario_responsavel_id) REFERENCES usuario(id)
);