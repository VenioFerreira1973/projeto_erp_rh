-- =====================================
-- TABELAS DE PERFIS E PERMISSÕES
-- =====================================
CREATE TABLE IF NOT EXISTS permissao (
                                         id BIGSERIAL PRIMARY KEY,
                                         descricao VARCHAR(255) UNIQUE NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS perfil (
                                      id BIGSERIAL PRIMARY KEY,
                                      descricao VARCHAR(255) UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS perfil_permissao (
                                                perfil_id BIGINT NOT NULL REFERENCES perfil(id),
    permissao_id BIGINT NOT NULL REFERENCES permissao(id),
    PRIMARY KEY(perfil_id, permissao_id)
    );

CREATE TABLE IF NOT EXISTS usuario (
                                       id BIGSERIAL PRIMARY KEY,
                                       login VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    senha_temporaria BOOLEAN NOT NULL DEFAULT TRUE,
    ativo BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS usuario_perfil (
                                              usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    perfil_id BIGINT NOT NULL REFERENCES perfil(id),
    PRIMARY KEY(usuario_id, perfil_id)
    );

-- =====================================
-- TABELAS DE DEPARTAMENTO, CARGO E FUNCIONÁRIO
-- =====================================
CREATE TABLE departamento (
                              id BIGSERIAL PRIMARY KEY,
                              descricao VARCHAR(40) NOT NULL,
                              data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                              data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE cargo (
                       id BIGSERIAL PRIMARY KEY,
                       nome VARCHAR(50) NOT NULL,
                       descricao VARCHAR(100) NOT NULL,
                       data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                       data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE funcionario (
                             id BIGSERIAL PRIMARY KEY,
                             nome VARCHAR(150) NOT NULL,
                             status VARCHAR(20) NOT NULL,
                             data_admissao DATE NOT NULL,
                             data_demissao DATE,
                             salario NUMERIC(12,2) NOT NULL,
                             cargo_id BIGINT REFERENCES cargo(id),
                             usuario_id BIGINT REFERENCES usuario(id),
                             gerente BOOLEAN DEFAULT FALSE, -- indica se é gerente do departamento
                             departamento_id BIGINT REFERENCES departamento(id),
                             data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                             data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                             CONSTRAINT fk_funcionario_gerente CHECK (
                                 NOT gerente OR departamento_id IS NOT NULL
                                 ) -- garante que só um funcionário com departamento pode ser gerente
);

-- =====================================
-- TABELAS DE FOLHA, FÉRIAS E LICENÇAS
-- =====================================
CREATE TABLE folha_pagamento (
                                 id BIGSERIAL PRIMARY KEY,
                                 funcionario_id BIGINT REFERENCES funcionario(id),
                                 periodo DATE,
                                 vencimentos NUMERIC(12,2),
                                 descontos NUMERIC(12,2),
                                 liquido NUMERIC(12,2),
                                 data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                                 data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE ferias (
                        id BIGSERIAL PRIMARY KEY,
                        funcionario_id BIGINT REFERENCES funcionario(id),
                        data_inicio DATE NOT NULL,
                        data_fim DATE NOT NULL,
                        status VARCHAR(20),
                        data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                        data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE TABLE licenca (
                         id BIGSERIAL PRIMARY KEY,
                         funcionario_id BIGINT REFERENCES funcionario(id),
                         tipo VARCHAR(30),
                         data_inicio DATE NOT NULL,
                         data_fim DATE NOT NULL,
                         status VARCHAR(20),
                         data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
                         data_alteracao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL
);

CREATE INDEX idx_usuario_login ON usuario(login);
CREATE INDEX idx_usuario_email ON usuario(email);

CREATE INDEX idx_funcionario_usuario ON funcionario(usuario_id);
CREATE INDEX idx_funcionario_cargo ON funcionario(cargo_id);
CREATE INDEX idx_funcionario_departamento ON funcionario(departamento_id);


-- =====================================
-- INSERÇÃO DE PERMISSÕES
-- =====================================
INSERT INTO permissao (descricao)
VALUES
    -- Funcionários
    ('FUNCIONARIO_READ'),
    ('FUNCIONARIO_WRITE'),
    ('FUNCIONARIO_ADMIN'),

    -- Estrutura organizacional
    ('ESTRUTURA_READ'),
    ('ESTRUTURA_MANAGE'),

    -- Folha de pagamento
    ('FOLHA_READ'),
    ('FOLHA_PROCESSAR'),
    ('FOLHA_ADMIN'),

    -- Férias e licenças
    ('AFASTAMENTO_READ'),
    ('AFASTAMENTO_SOLICITAR'),
    ('AFASTAMENTO_APROVAR'),

    -- Segurança
    ('USUARIO_ADMIN'),
    ('PERFIL_ADMIN')
    ON CONFLICT (descricao) DO NOTHING;

-- =====================================
-- INSERÇÃO DE PERFIS
-- =====================================
INSERT INTO perfil (descricao, ativo)
VALUES
    ('ADMIN', true),
    ('RH', true),
    ('FINANCEIRO', true),
    ('GESTOR', true),
    ('FUNCIONARIO', true)
    ON CONFLICT (descricao) DO NOTHING;

-- =====================================
-- ASSOCIAÇÃO PERFIL ↔ PERMISSÃO
-- =====================================
-- ADMIN recebe todas as permissões
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, pe.id
FROM perfil p
         CROSS JOIN permissao pe
WHERE p.descricao = 'ADMIN'
    ON CONFLICT DO NOTHING;

-- FUNCIONARIO RH
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, pe.id
FROM perfil p
         JOIN permissao pe ON pe.descricao IN (
                                               'FUNCIONARIO_READ',
                                               'FUNCIONARIO_WRITE',
                                               'AFASTAMENTO_READ',
                                               'AFASTAMENTO_APROVAR'
    )
WHERE p.descricao = 'RH'
    ON CONFLICT DO NOTHING;

-- FUNCIONARIO FINANCEIRO
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, pe.id
FROM perfil p
         JOIN permissao pe ON pe.descricao IN (
                                               'FOLHA_READ',
                                               'FOLHA_PROCESSAR'
    )
WHERE p.descricao = 'FINANCEIRO'
    ON CONFLICT DO NOTHING;


-- FUNCIONARIO GESTOR
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, pe.id
FROM perfil p
         JOIN permissao pe ON pe.descricao IN (
                                               'FUNCIONARIO_READ',
                                               'AFASTAMENTO_READ',
                                               'AFASTAMENTO_APROVAR'
    )
WHERE p.descricao = 'GESTOR'
    ON CONFLICT DO NOTHING;

--FUNCIONARIO COMUM
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, pe.id
FROM perfil p
         JOIN permissao pe ON pe.descricao IN (
                                               'FUNCIONARIO_READ',
                                               'AFASTAMENTO_SOLICITAR'
    )
WHERE p.descricao = 'FUNCIONARIO'
    ON CONFLICT DO NOTHING;

-- =====================================
-- INSERÇÃO DE USUÁRIOS
-- =====================================
INSERT INTO usuario (login, email, senha, senha_temporaria, ativo)
VALUES
    ('admin', 'admin@empresa.com', '$2a$10$v4tbe/Rh1uoVswY3L39GDu84/x4srUw0uxP4ttY8Kb4I31sz2CgiC', false, true),
    ('venio', 'venio@empresa.com', '$2a$10$v4tbe/Rh1uoVswY3L39GDu84/x4srUw0uxP4ttY8Kb4I31sz2CgiC', false, true)
    ON CONFLICT (login) DO NOTHING;


-- =====================================
-- ASSOCIAÇÃO USUÁRIO ↔ PERFIL
-- =====================================
-- admin → ADMIN
INSERT INTO usuario_perfil (usuario_id, perfil_id)
SELECT u.id, p.id
FROM usuario u
         JOIN perfil p ON p.descricao = 'ADMIN'
WHERE u.login = 'admin'
    ON CONFLICT DO NOTHING;

-- venio → FUNCIONARIO
INSERT INTO usuario_perfil (usuario_id, perfil_id)
SELECT u.id, p.id
FROM usuario u
         JOIN perfil p ON p.descricao = 'FUNCIONARIO'
WHERE u.login = 'venio'
    ON CONFLICT DO NOTHING;

-- =====================================
-- INSERÇÃO DE DEPARTAMENTOS
-- =====================================
INSERT INTO departamento (descricao)
SELECT 'TI'
    WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE descricao = 'TI');

INSERT INTO departamento (descricao)
SELECT 'Financeiro'
    WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE descricao = 'Financeiro');

INSERT INTO departamento (descricao)
SELECT 'RH'
    WHERE NOT EXISTS (SELECT 1 FROM departamento WHERE descricao = 'RH');

-- =====================================
-- INSERÇÃO DE CARGOS
-- =====================================
INSERT INTO cargo (nome, descricao)
SELECT 'Analista de Sistemas', 'Responsável por desenvolvimento e manutenção de sistemas'
    WHERE NOT EXISTS (
    SELECT 1 FROM cargo WHERE nome = 'Analista de Sistemas'
);

INSERT INTO cargo (nome, descricao)
SELECT 'Gerente de TI', 'Responsável pelo departamento de TI'
    WHERE NOT EXISTS (
    SELECT 1 FROM cargo WHERE nome = 'Gerente de TI'
);

INSERT INTO cargo (nome, descricao)
SELECT 'Assistente Administrativo', 'Auxilia nas tarefas administrativas'
    WHERE NOT EXISTS (
    SELECT 1 FROM cargo WHERE nome = 'Assistente Administrativo'
);

-- =====================================
-- INSERÇÃO DE FUNCIONÁRIOS
-- =====================================
INSERT INTO funcionario (usuario_id, nome, status, data_admissao, salario, cargo_id, gerente, departamento_id)
VALUES
    (1, 'Administrador', 'ATIVO', '2025-01-01', 5000.00, 2, true, 1), -- gerente TI
    (2, 'Venio', 'ATIVO', '2025-01-10', 3000.00, 1, false, 1)
    ON CONFLICT (id) DO NOTHING;

-- =====================================
-- INSERÇÃO DE FOLHA DE PAGAMENTO
-- =====================================
INSERT INTO folha_pagamento (funcionario_id, periodo, vencimentos, descontos, liquido)
VALUES
    (1, '2025-01-31', 5000.00, 500.00, 4500.00),
    (2, '2025-01-31', 3000.00, 300.00, 2700.00)
    ON CONFLICT (id) DO NOTHING;

-- =====================================
-- INSERÇÃO DE FÉRIAS
-- =====================================
INSERT INTO ferias (funcionario_id, data_inicio, data_fim, status)
VALUES
    (2, '2025-07-01', '2025-07-15', 'AGENDADA')
    ON CONFLICT (id) DO NOTHING;

-- =====================================
-- INSERÇÃO DE LICENÇAS
-- =====================================
INSERT INTO licenca (funcionario_id, tipo, data_inicio, data_fim, status)
VALUES
    (2, 'Licença Médica', '2025-03-01', '2025-03-10', 'APROVADA')
    ON CONFLICT (id) DO NOTHING;
