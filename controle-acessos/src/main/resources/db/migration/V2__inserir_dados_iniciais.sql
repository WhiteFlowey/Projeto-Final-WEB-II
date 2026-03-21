-- =================================================================================
-- 1. INSERIR DEPARTAMENTOS
-- =================================================================================
INSERT INTO departamento (nome) VALUES ('Tecnologia da Informação');
INSERT INTO departamento (nome) VALUES ('Administração');
INSERT INTO departamento (nome) VALUES ('Recursos Humanos');

-- =================================================================================
-- 2. INSERIR PERFIS (As Roles do Spring Security)
-- Dica: O Spring Security por padrão gosta que os perfis comecem com "ROLE_"
-- =================================================================================
INSERT INTO perfil (nome, descricao) VALUES ('ROLE_ADMIN', 'Administrador do sistema com acesso total a todos os módulos');
INSERT INTO perfil (nome, descricao) VALUES ('ROLE_GESTOR', 'Gestor responsável por aprovar ou rejeitar acessos aos módulos');
INSERT INTO perfil (nome, descricao) VALUES ('ROLE_COMUM', 'Usuário comum que precisa solicitar acesso aos módulos');

-- =================================================================================
-- 3. INSERIR USUÁRIO ADMINISTRADOR DE TESTE
-- A senha salva aqui é o hash BCrypt para a senha: "123456"
-- =================================================================================
INSERT INTO usuario (nome, email, senha, status, departamento_id, perfil_id) 
VALUES (
    'Bruno', 
    'admin@gatekeeper.com', 
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.', 
    'ATIVO', 
    1, -- Referência ao departamento de TI
    1  -- Referência ao perfil ROLE_ADMIN
);