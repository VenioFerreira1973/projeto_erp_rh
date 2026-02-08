-- =====================================
-- ENUMERATIONS
-- =====================================
CREATE TYPE tipo_vinculo AS ENUM (
    'CLT', 'ESTAGIARIO', 'APRENDIZ', 'AUTONOMO', 'COOPERADO', 'TEMPORARIO', 'TERCEIRIZADO'
);

CREATE TYPE regime_trabalho AS ENUM (
    'PRESENCIAL', 'HIBRIDO', 'REMOTO'
);

CREATE TYPE contrato_status AS ENUM (
    'ATIVO', 'AFASTADO', 'SUSPENSO', 'RESCINDIDO'
);

CREATE TYPE status AS ENUM (
    'ATIVO', 'INATIVO'
);

CREATE TYPE usuario_status AS ENUM (
    'ATIVO', 'INATIVO'
);

CREATE TYPE motivo_demissao AS ENUM(
    'SEM_JUSTA_CAUSA', 'POR_JUSTA_CAUSA', 'PEDIDO_DEMISSAO', 'RESCISAO_COMUM_ACORDO', 'RESCISAO_INDIRETA'
);

CREATE TYPE motivo_alteracao_salario AS ENUM (
    'ADMISSAO', 'REAJUSTE_SALARIO_MINIMO', 'DISSIDIO', 'ACORDO_COLETIVO', 'PISO_SALARIAL_ESTADUAL', 'PROMOCAO', 'MUDANCA_CARGO', 'SALARIO_SUBSTITUICAO', 'ADICIONAL_QUALIFICACAO'
);

CREATE TYPE nivel_cargo AS ENUM (
    'ESTAGIARIO', 'TRAINEE', 'JUNIOR1', 'JUNIOR2', 'JUNIOR3', 'PLENO1', 'PLENO2', 'PLENO3', 'SENIOR1', 'SENIOR2', 'SENIOR3', 'ESPECIALISTA'
);

CREATE TYPE tipo_jornada AS ENUM (
    'INTEGRAL', 'PARCIAL', 'TURNO', 'ESCALA', 'NOTURNA', 'REVEZAMENTO'
);

CREATE TYPE tipo_contrato AS ENUM (
    'PADRAO', 'TELETRABALHO', 'VERDE_AMARELO'
);

CREATE TYPE prazo_contrato AS ENUM (
    'PRAZO_INDETERMINADO', 'PRAZO_DETERMINADO', 'INTERMITENTE', 'TEMPORARIO'
);

CREATE TYPE estado_civil AS ENUM (
    'SOLTEIRO', 'CASADO', 'DIVORCIADO', 'VIUVO'
);

CREATE TYPE genero AS ENUM (
    'MASCULINO', 'FEMININO', 'OUTRO'
);

CREATE TYPE cor_raca AS ENUM (
    'BRANCO', 'NEGRO', 'PARDO', 'INDIGENA', 'AMARELO'
);

CREATE TYPE tipo_endereco AS ENUM (
    'RESIDENCIAL', 'COMERCIAL', 'CORRESPONDENCIA'
);

CREATE TYPE tipo_conta AS ENUM (
    'SALARIO', 'CORRENTE', 'PAGAMENTO', 'POUPANCA'
);

CREATE TYPE forma_pagamento AS ENUM (
    'TED', 'PIX', 'CHEQUE'
);

CREATE TYPE uf AS ENUM (
    'AC', 'AL', 'AP', 'AM', 'BA', 'CE', 'DF', 'ES', 'GO', 'MA',
    'MT', 'MS', 'MG', 'PA', 'PB', 'PR', 'PE', 'PI', 'RJ', 'RN',
    'RS', 'RO', 'RR', 'SC', 'SP', 'SE', 'TO'
);

CREATE TYPE tipo_documento_anexo AS ENUM (
    'RG', 'CPF', 'CNH', 'CTPS', 'PASSAPORTE', 'COMPROVANTE_RESIDENCIA',
    'CERTIFICADO', 'FOTO_PERFIL', 'OUTRO'
);

CREATE TYPE status_validacao AS ENUM (
    'PENDENTE', 'APROVADO', 'REPROVADO'
);

CREATE TYPE onboarding_validacao_step AS ENUM (
    'DADOS_PESSOAIS', 'ENDERECO', 'DOCUMENTOS', 'DADOS_BANCARIOS', 'DOCUMENTOS_ANEXOS'
);

CREATE TYPE onboarding_step AS ENUM (
    'DADOS_PESSOAIS', 'ENDERECO', 'DOCUMENTOS', 'DOCUMENTOS_ANEXOS', 'DADOS_BANCARIOS', 'EM_ANALISE', 'CONCLUIDO'
);

CREATE TYPE tipo_dependente AS ENUM (
    'FILHO', 'CONJUGE', 'OUTRO'
);


-- =====================================
-- TABLES
-- =====================================
CREATE TABLE IF NOT EXISTS empresa
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    nome_fantasia
    VARCHAR
(
    150
) NOT NULL,
    razao_social VARCHAR
(
    150
) NOT NULL,
    cnpj CHAR
(
    14
) NOT NULL UNIQUE,
    status VARCHAR
(
    20
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS cargo
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    nome
    VARCHAR
(
    40
) NOT NULL UNIQUE,
    descricao VARCHAR
(
    200
) NOT NULL,
    status VARCHAR
(
    20
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS departamento
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    descricao
    VARCHAR
(
    100
) NOT NULL,
    status VARCHAR
(
    20
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS centro_custo
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    nome
    VARCHAR
(
    30
) NOT NULL,
    descricao VARCHAR
(
    100
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS sindicato
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    nome
    VARCHAR
(
    30
) NOT NULL,
    descricao VARCHAR
(
    100
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS permissao
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    descricao
    VARCHAR
(
    255
) UNIQUE NOT NULL,
    status VARCHAR
(
    20
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS perfil
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    descricao
    VARCHAR
(
    255
) UNIQUE NOT NULL,
    status VARCHAR
(
    20
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE DEFAULT now() NOT NULL,
    data_alteracao TIMESTAMP
                           WITH TIME ZONE DEFAULT now() NOT NULL
    );

CREATE TABLE IF NOT EXISTS perfil_permissao
(
    perfil_id
    BIGINT
    NOT
    NULL
    REFERENCES
    perfil
(
    id
),
    permissao_id BIGINT NOT NULL REFERENCES permissao
(
    id
),
    PRIMARY KEY
(
    perfil_id,
    permissao_id
)
    );

CREATE TABLE IF NOT EXISTS usuario
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    login
    VARCHAR
(
    100
) NOT NULL UNIQUE,
    email_pessoal VARCHAR
(
    100
) NOT NULL,
    senha VARCHAR
(
    255
) NOT NULL,
    primeiro_acesso boolean NOT NULL DEFAULT TRUE,
    usuario_status VARCHAR
(
    20
) NOT NULL DEFAULT 'ATIVO',
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    data_alteracao TIMESTAMP
                           WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS usuario_perfil
(
    usuario_id
    BIGINT
    NOT
    NULL
    REFERENCES
    usuario
(
    id
),
    perfil_id BIGINT NOT NULL REFERENCES perfil
(
    id
),
    PRIMARY KEY
(
    usuario_id,
    perfil_id
)
    );

CREATE TABLE IF NOT EXISTS colaborador
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    nome
    VARCHAR
(
    255
) NOT NULL,
    matricula VARCHAR
(
    20
) NOT NULL UNIQUE,
    email_corporativo VARCHAR
(
    100
) NOT NULL UNIQUE,
    usuario_id BIGINT REFERENCES usuario
(
    id
),
    onboarding_step VARCHAR
(
    20
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    data_alteracao TIMESTAMP
                           WITH TIME ZONE NOT NULL DEFAULT now()
    );

CREATE TABLE IF NOT EXISTS colaborador_lotacao
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    colaborador_id
    BIGINT
    NOT
    NULL
    UNIQUE
    REFERENCES
    colaborador
(
    id
),
    cargo_id BIGINT REFERENCES cargo
(
    id
),
    departamento_id BIGINT REFERENCES departamento
(
    id
),
    gestor_id BIGINT REFERENCES colaborador
(
    id
),
    centro_custo_id BIGINT REFERENCES centro_custo
(
    id
),
    cargo_cbo VARCHAR
(
    255
),
    nivel_cargo VARCHAR
(
    20
),
    data_inicio TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    data_fim TIMESTAMP WITH TIME ZONE,
    data_criacao TIMESTAMP
                          WITH TIME ZONE NOT NULL DEFAULT now(),
    data_alteracao TIMESTAMP
                          WITH TIME ZONE NOT NULL DEFAULT now()
    );


CREATE TABLE IF NOT EXISTS colaborador_contrato
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    colaborador_id
    BIGINT
    NOT
    NULL
    UNIQUE
    REFERENCES
    colaborador
(
    id
),
    sindicato_id BIGINT REFERENCES sindicato
(
    id
),
    empresa_id BIGINT NOT NULL REFERENCES empresa
(
    id
),
    tipo_jornada VARCHAR
(
    20
),
    horas_semanais INTEGER,
    tipo_contrato VARCHAR
(
    20
) NOT NULL,
    prazo_contrato VARCHAR
(
    20
),
    experiencia_inicio DATE,
    experiencia_fim DATE,
    tipo_vinculo VARCHAR
(
    20
) NOT NULL,
    regime_trabalho VARCHAR
(
    20
) NOT NULL,
    contrato_status VARCHAR
(
    20
) NOT NULL,
    primeiro_emprego BOOLEAN NOT NULL DEFAULT FALSE,
    data_admissao DATE NOT NULL,
    data_inicio_vinculo DATE NOT NULL,
    data_demissao DATE,
    data_fim_vinculo DATE,
    motivo_demissao VARCHAR
(
    30
),
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    data_alteracao TIMESTAMP
                           WITH TIME ZONE NOT NULL DEFAULT now()
    );


CREATE TABLE IF NOT EXISTS colaborador_salario
(
    id
    BIGSERIAL
    PRIMARY
    KEY,
    colaborador_contrato_id
    BIGINT
    NOT
    NULL
    REFERENCES
    colaborador_contrato
(
    id
),
    valor NUMERIC
(
    12,
    2
) NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    motivo_alteracao_salario VARCHAR
(
    30
) NOT NULL,
    data_criacao TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    data_alteracao TIMESTAMP
                           WITH TIME ZONE NOT NULL DEFAULT now()
    );

--- =====================================
-- Sequence para token de reset de senha
-- =====================================
CREATE SEQUENCE password_reset_token_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE CACHE 1;

CREATE TABLE password_reset_token
(
    id         BIGINT       NOT NULL DEFAULT nextval('password_reset_token_seq'),
    token      VARCHAR(255) NOT NULL UNIQUE,
    usuario_id BIGINT       NOT NULL,
    expira_em  TIMESTAMP    NOT NULL,
    usado      BOOLEAN               DEFAULT FALSE,

    CONSTRAINT pk_password_reset_token PRIMARY KEY (id),
    CONSTRAINT fk_password_reset_token_usuario
        FOREIGN KEY (usuario_id)
            REFERENCES usuario (id)
            ON DELETE CASCADE
);

CREATE TABLE colaborador_dados_pessoais
(
    id              BIGSERIAL PRIMARY KEY,
    colaborador_id  BIGINT       NOT NULL UNIQUE,
    data_nascimento DATE         NOT NULL,
    estado_civil    VARCHAR(30),
    genero          VARCHAR(30),
    cor_raca        VARCHAR(30),
    nacionalidade   VARCHAR(255) NOT NULL,
    status          VARCHAR(10)  NOT NULL,
    data_criacao    TIMESTAMPTZ  NOT NULL,
    data_alteracao  TIMESTAMPTZ,
    CONSTRAINT fk_colaborador_dados_pessoais_colaborador
        FOREIGN KEY (colaborador_id)
            REFERENCES colaborador (id)
            ON DELETE CASCADE
);

CREATE TABLE colaborador_endereco
(
    id             BIGSERIAL PRIMARY KEY,
    colaborador_id BIGINT       NOT NULL,
    tipo_endereco  VARCHAR(20)  NOT NULL,
    cep            VARCHAR(8)   NOT NULL,
    logradouro     VARCHAR(150) NOT NULL,
    numero         VARCHAR(10)  NOT NULL,
    complemento    VARCHAR(100),
    bairro         VARCHAR(100) NOT NULL,
    municipio      VARCHAR(100) NOT NULL,
    uf             CHAR(2)      NOT NULL,
    pais           VARCHAR(60)  NOT NULL,
    status         VARCHAR(10)  NOT NULL,
    data_criacao   TIMESTAMPTZ  NOT NULL,
    data_alteracao TIMESTAMPTZ,
    CONSTRAINT fk_colab_end_colaborador
        FOREIGN KEY (colaborador_id)
            REFERENCES colaborador (id)
);

CREATE TABLE colaborador_dados_bancarios
(
    id              BIGSERIAL PRIMARY KEY,
    colaborador_id  BIGINT       NOT NULL,
    banco_codigo    VARCHAR(10)  NOT NULL,
    banco_nome      VARCHAR(100) NOT NULL,
    agencia         VARCHAR(20)  NOT NULL,
    conta           VARCHAR(20)  NOT NULL,
    digito_conta    VARCHAR(5)   NOT NULL,
    tipo_conta      VARCHAR(30)  NOT NULL,
    chave_pix       VARCHAR(255),
    forma_pagamento VARCHAR(30)  NOT NULL,
    status          VARCHAR(10)  NOT NULL DEFAULT 'ATIVO',
    data_criacao    TIMESTAMPTZ  NOT NULL,
    data_alteracao  TIMESTAMPTZ,
    CONSTRAINT fk_colaborador_dados_bancarios_colaborador
        FOREIGN KEY (colaborador_id)
            REFERENCES colaborador (id)
            ON UPDATE RESTRICT
            ON DELETE RESTRICT
);

CREATE TABLE colaborador_documentos
(
    id                   BIGSERIAL PRIMARY KEY,
    colaborador_id       BIGINT      NOT NULL,
    cpf                  VARCHAR(11) NOT NULL,
    pis_pasep            VARCHAR(20) NOT NULL,
    possui_ctps_fisica   BOOLEAN     NOT NULL,
    ctps_numero          VARCHAR(20),
    ctps_serie           VARCHAR(5),
    registro_estrangeiro VARCHAR(30),
    data_criacao         TIMESTAMPTZ NOT NULL,
    data_alteracao       TIMESTAMPTZ,
    status               VARCHAR(10) NOT NULL,
    CONSTRAINT uk_colaborador_documentos_cpf UNIQUE (cpf),
    CONSTRAINT uk_colaborador_documentos_colaborador UNIQUE (colaborador_id),
    CONSTRAINT fk_colaborador_documentos_colaborador
        FOREIGN KEY (colaborador_id)
            REFERENCES colaborador (id)
);

CREATE TABLE colaborador_dependentes
(
    id                          BIGSERIAL PRIMARY KEY,
    colaborador_id              BIGINT                   NOT NULL,
    tipo_dependente             VARCHAR(255)             NOT NULL,
    nome                        VARCHAR(255)             NOT NULL,
    cpf                         VARCHAR(11),
    data_nascimento             DATE                     NOT NULL,
    dependencia_ir              BOOLEAN                  NOT NULL,
    dependencia_salario_familia BOOLEAN                  NOT NULL,
    status                      VARCHAR(10)              NOT NULL DEFAULT 'ATIVO',
    data_criacao                TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    data_alteracao              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_colaborador
        FOREIGN KEY (colaborador_id)
            REFERENCES colaborador (id)
            ON DELETE RESTRICT
);

CREATE TABLE colaborador_documento_anexo
(
    id                   BIGSERIAL PRIMARY KEY,
    colaborador_id       BIGINT                   NOT NULL,
    tipo_documento_anexo VARCHAR(255)             NOT NULL,
    arquivo_url          TEXT                     NOT NULL,
    data_upload          TIMESTAMP WITH TIME ZONE,
    data_validade        DATE,
    status               VARCHAR(10)              NOT NULL DEFAULT 'ATIVO',
    data_criacao         TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    data_alteracao       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    CONSTRAINT fk_colaborador
        FOREIGN KEY (colaborador_id)
            REFERENCES colaborador (id)
);


CREATE TABLE onboarding_validacoes
(
    id                        BIGSERIAL PRIMARY KEY,
    colaborador_id            BIGINT      NOT NULL,
    onboarding_validacao_step VARCHAR(50) NOT NULL,
    status_validacao          VARCHAR(15) NOT NULL,
    observacao                TEXT,
    validado_por              BIGINT,
    validado_em               TIMESTAMPTZ,
    data_criacao              TIMESTAMPTZ NOT NULL,
    data_alteracao            TIMESTAMPTZ NOT NULL,
    CONSTRAINT uk_onboarding_validacoes_colaborador_onboarding_validacao_step
        UNIQUE (colaborador_id, onboarding_validacao_step)
);

-- =====================================
-- INDEXES
-- =====================================
CREATE INDEX idx_colaborador_empresa ON colaborador_contrato (empresa_id);
CREATE INDEX idx_func_sal_contrato ON colaborador_salario (colaborador_contrato_id);
CREATE INDEX idx_func_sal_vigente ON colaborador_salario (colaborador_contrato_id) WHERE data_fim IS NULL;
CREATE UNIQUE INDEX ux_contrato_ativo ON colaborador_contrato (colaborador_id) WHERE contrato_status = 'ATIVO';
CREATE UNIQUE INDEX idx_lotacao_ativa ON colaborador_lotacao (colaborador_id) WHERE data_fim IS NULL;

CREATE INDEX idx_colab_end_colaborador ON colaborador_endereco (colaborador_id);
CREATE INDEX idx_colab_end_status ON colaborador_endereco (status);
CREATE INDEX idx_colab_end_tipo ON colaborador_endereco (tipo_endereco);
CREATE INDEX idx_colab_end_colab_tipo_status ON colaborador_endereco (colaborador_id, tipo_endereco, status);

CREATE INDEX idx_cdb_colaborador ON colaborador_dados_bancarios (colaborador_id);
CREATE INDEX idx_cdb_status ON colaborador_dados_bancarios (status);

CREATE INDEX idx_colaborador_documentos_status ON colaborador_documentos (status);
CREATE INDEX idx_colaborador_documentos_pis_pasep ON colaborador_documentos (pis_pasep);
CREATE INDEX idx_colaborador_documentos_colaborador_id ON colaborador_documentos (colaborador_id);

CREATE INDEX idx_colaborador_dependentes_colaborador_id ON colaborador_dependentes (colaborador_id);
CREATE INDEX idx_colaborador_dependentes_status ON colaborador_dependentes (status);

CREATE INDEX idx_colaborador_documento_anexo_colaborador_id ON colaborador_documento_anexo (colaborador_id);

CREATE INDEX idx_colaborador_documento_anexo_status ON colaborador_documento_anexo (status);

-- =====================================
-- INSERTS DE TESTE
-- =====================================

-- Empresas
INSERT INTO empresa (nome_fantasia, razao_social, cnpj, status)
VALUES ('Tech Solutions', 'Tech Solutions LTDA', '12345678000199', 'ATIVO'),
       ('Alpha Sistemas', 'Alpha Sistemas S.A.', '98765432000188', 'ATIVO') ON CONFLICT (cnpj) DO NOTHING;

-- Permissões
INSERT INTO permissao (descricao, status)
VALUES ('COLABORADOR_READ', 'ATIVO'),
       ('COLABORADOR_WRITE', 'ATIVO'),
       ('COLABORADOR_ADMIN', 'ATIVO'),
       ('ESTRUTURA_READ', 'ATIVO'),
       ('ESTRUTURA_MANAGE', 'ATIVO'),
       ('FOLHA_READ', 'ATIVO'),
       ('FOLHA_PROCESSAR', 'ATIVO'),
       ('FOLHA_ADMIN', 'ATIVO'),
       ('AFASTAMENTO_READ', 'ATIVO'),
       ('AFASTAMENTO_SOLICITAR', 'ATIVO'),
       ('AFASTAMENTO_APROVAR', 'ATIVO'),
       ('USUARIO_ADMIN', 'ATIVO'),
       ('PERFIL_ADMIN', 'ATIVO'),
       ('ONBOARDING_WRITE', 'ATIVO'),
       ('ONBOARDING_READ', 'ATIVO') ON CONFLICT (descricao) DO NOTHING;


-- Perfis
INSERT INTO perfil (descricao, status)
VALUES ('ADMIN', 'ATIVO'),
       ('RH', 'ATIVO'),
       ('FINANCEIRO', 'ATIVO'),
       ('GESTOR', 'ATIVO'),
       ('COLABORADOR', 'ATIVO'),
       ('CANDIDATO', 'ATIVO') ON CONFLICT (descricao) DO NOTHING;


-- Usuários
INSERT INTO usuario (login, email_pessoal, senha, primeiro_acesso, usuario_status, data_criacao, data_alteracao)
VALUES ('admin', 'venioferreira@gmail.com', '$2a$10$v4tbe/Rh1uoVswY3L39GDu84/x4srUw0uxP4ttY8Kb4I31sz2CgiC', false,
        'ATIVO',
        NOW(), NOW()),
       ('venio', 'venioferreira@gmail.com', '$2a$10$v4tbe/Rh1uoVswY3L39GDu84/x4srUw0uxP4ttY8Kb4I31sz2CgiC', false,
        'ATIVO',
        NOW(), NOW()) ON CONFLICT (login) DO NOTHING;

-- Associação Usuário ↔ Perfil
INSERT INTO usuario_perfil (usuario_id, perfil_id)
SELECT u.id, p.id
FROM usuario u
         CROSS JOIN perfil p
WHERE (u.login = 'admin' AND p.descricao = 'ADMIN')
   OR (u.login = 'venio' AND p.descricao = 'COLABORADOR') ON CONFLICT DO NOTHING;

INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p,
     permissao per
WHERE p.descricao = 'ADMIN' ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p,
     permissao per
WHERE p.descricao = 'COLABORADOR'
  AND per.descricao = 'COLABORADOR_READ' ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- RH → gestão de funcionários e estrutura
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p,
     permissao per
WHERE p.descricao = 'RH'
  AND per.descricao IN ('COLABORADOR_READ', 'COLABORADOR_WRITE', 'ESTRUTURA_READ',
                        'ESTRUTURA_MANAGE')
    ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- FINANCEIRO → folha e estrutura
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p,
     permissao per
WHERE p.descricao = 'FINANCEIRO'
  AND per.descricao IN ('FOLHA_READ', 'FOLHA_PROCESSAR', 'FOLHA_ADMIN') ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- GESTOR → aprovações e leitura geral
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p,
     permissao per
WHERE p.descricao = 'GESTOR'
  AND per.descricao IN ('COLABORADOR_READ', 'FOLHA_READ', 'AFASTAMENTO_READ',
                        'AFASTAMENTO_APROVAR')
    ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- COLABORADOR → acesso básico após onboarding
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p
         JOIN permissao per
              ON per.descricao IN (
                  'COLABORADOR_READ'
                  )
WHERE p.descricao = 'COLABORADOR' ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- CANDIDATO → onboarding (dados pessoais)
INSERT INTO perfil_permissao (perfil_id, permissao_id)
SELECT p.id, per.id
FROM perfil p
         JOIN permissao per
              ON per.descricao IN (
                                   'ONBOARDING_READ',
                                   'ONBOARDING_WRITE'
                  )
WHERE p.descricao = 'CANDIDATO' ON CONFLICT (perfil_id, permissao_id) DO NOTHING;


-- Departamentos
INSERT INTO departamento (descricao, status)
VALUES ('TI', 'ATIVO'),
       ('Financeiro', 'ATIVO'),
       ('RH', 'ATIVO') ON CONFLICT DO NOTHING;

-- Cargos
INSERT INTO cargo (nome, descricao, status)
VALUES ('Desenvolvedor Backend', 'Responsável pelo desenvolvimento de APIs e regras de negócio', 'ATIVO'),
       ('Desenvolvedor Frontend', 'Responsável pela interface e experiência do usuário', 'ATIVO'),
       ('Desenvolvedor Full Stack', 'Atua tanto no backend quanto no frontend', 'ATIVO'),
       ('Analista de Sistemas', 'Analisa e especifica requisitos de sistemas', 'ATIVO'),
       ('Engenheiro de Software', 'Projeta e desenvolve soluções de software escaláveis', 'ATIVO'),
       ('Administrador de Banco de Dados', 'Gerencia e otimiza bancos de dados', 'ATIVO'),
       ('Analista de Segurança da Informação', 'Garante a segurança dos sistemas e dados', 'ATIVO'),
       ('DevOps Engineer', 'Automatiza processos de deploy e infraestrutura', 'ATIVO'),
       ('Suporte Técnico', 'Presta suporte técnico a usuários e sistemas', 'ATIVO'),
       ('Product Owner', 'Define prioridades e requisitos do produto', 'ATIVO') ON CONFLICT DO NOTHING;

INSERT INTO sindicato (nome, descricao, data_criacao, data_alteracao)
VALUES ('Sindicato TI', 'Sindicato dos Profissionais de TI', NOW(), NOW()),
       ('Sindicato Financeiro', 'Sindicato do Setor Financeiro', NOW(), NOW()) ON CONFLICT DO NOTHING;


-- Colaboradores
INSERT INTO colaborador (nome, matricula, email_corporativo, usuario_id, onboarding_step, data_criacao, data_alteracao)
SELECT 'Administrador', 'MAT000001', 'administrador@empresa.com', u.id, 'CONCLUIDO', NOW(), NOW()
FROM usuario u
WHERE u.login = 'admin' ON CONFLICT (matricula) DO NOTHING;

INSERT INTO colaborador (nome, matricula, email_corporativo, usuario_id, onboarding_step, data_criacao, data_alteracao)
SELECT 'Venio', 'MAT000002', 'venio@empresa.com', u.id, 'CONCLUIDO', NOW(), NOW()
FROM usuario u
WHERE u.login = 'venio' ON CONFLICT (matricula) DO NOTHING;


-- =====================================
-- DADOS PESSOAIS DOS FUNCIONÁRIOS
-- =====================================
-- Inserindo dados pessoais para o Administrador
INSERT INTO colaborador_dados_pessoais (colaborador_id,
                                        data_nascimento,
                                        estado_civil,
                                        genero,
                                        cor_raca,
                                        nacionalidade,
                                        status,
                                        data_criacao,
                                        data_alteracao)
SELECT f.id,
       DATE '1980-01-01' AS data_nascimento,
       'CASADO'::estado_civil AS estado_civil, 'MASCULINO'::genero AS genero, 'BRANCO'::cor_raca AS cor_raca, 'Brasileiro' AS nacionalidade,
       'ATIVO'           AS status,
       NOW()             AS data_criacao,
       NULL              AS data_alteracao
FROM colaborador f
WHERE f.matricula = 'MAT000001' ON CONFLICT (colaborador_id) DO NOTHING;

-- Inserindo dados pessoais para o Venio
INSERT INTO colaborador_dados_pessoais (colaborador_id,
                                        data_nascimento,
                                        estado_civil,
                                        genero,
                                        cor_raca,
                                        nacionalidade,
                                        status,
                                        data_criacao,
                                        data_alteracao)
SELECT f.id,
       DATE '1990-05-15' AS data_nascimento,
       'SOLTEIRO'::estado_civil AS estado_civil, 'MASCULINO'::genero AS genero, 'PARDO'::cor_raca AS cor_raca, 'Brasileiro' AS nacionalidade,
       'ATIVO'           AS status,
       NOW()             AS data_criacao,
       NULL              AS data_alteracao
FROM colaborador f
WHERE f.matricula = 'MAT000002' ON CONFLICT (colaborador_id) DO NOTHING;


-- =====================================
-- CONTRATOS DOS FUNCIONÁRIOS
-- =====================================

-- Contrato do Administrador (MAT001)
INSERT INTO colaborador_contrato (colaborador_id,
                                  sindicato_id,
                                  empresa_id,
                                  tipo_jornada,
                                  horas_semanais,
                                  tipo_contrato,
                                  prazo_contrato,
                                  experiencia_inicio,
                                  experiencia_fim,
                                  tipo_vinculo,
                                  regime_trabalho,
                                  contrato_status,
                                  primeiro_emprego,
                                  data_admissao,
                                  data_inicio_vinculo,
                                  data_criacao,
                                  data_alteracao)
SELECT f.id,
       s.id,
       e.id,
       'INTEGRAL',
       40,
       'PADRAO',
       'PRAZO_INDETERMINADO',
       NULL,
       NULL,
       'CLT',
       'PRESENCIAL',
       'ATIVO',
       FALSE,
       DATE '2025-01-01',
       DATE '2025-01-01',
       NOW(),
       NOW()
FROM colaborador f
         JOIN empresa e ON e.nome_fantasia = 'Tech Solutions'
         LEFT JOIN sindicato s ON s.nome = 'Sindicato TI'
WHERE f.matricula = 'MAT001' ON CONFLICT (colaborador_id) DO NOTHING;


-- Contrato do Venio (MAT002)
INSERT INTO colaborador_contrato (colaborador_id,
                                  sindicato_id,
                                  empresa_id,
                                  tipo_jornada,
                                  horas_semanais,
                                  tipo_contrato,
                                  prazo_contrato,
                                  experiencia_inicio,
                                  experiencia_fim,
                                  tipo_vinculo,
                                  regime_trabalho,
                                  contrato_status,
                                  primeiro_emprego,
                                  data_admissao,
                                  data_inicio_vinculo,
                                  data_criacao,
                                  data_alteracao)
SELECT f.id,
       s.id,
       e.id,
       'INTEGRAL',
       40,
       'PADRAO',
       'PRAZO_INDETERMINADO',
       DATE '2025-01-10',
       DATE '2025-04-10',
       'CLT',
       'REMOTO',
       'ATIVO',
       TRUE,
       DATE '2025-01-10',
       DATE '2025-01-10',
       NOW(),
       NOW()
FROM colaborador f
         JOIN empresa e ON e.nome_fantasia = 'Tech Solutions'
         LEFT JOIN sindicato s ON s.nome = 'Sindicato TI'
WHERE f.matricula = 'MAT002' ON CONFLICT (colaborador_id) DO NOTHING;


-- Cento de custo de exemplo
INSERT INTO centro_custo (nome, descricao, data_criacao, data_alteracao)
VALUES ('TI01', 'Centro de Tecnologia', NOW(), NOW()),
       ('FIN01', 'Centro Financeiro', NOW(), NOW()) ON CONFLICT DO NOTHING;

-- Lotação do Administrador (gestor de TI)
INSERT INTO colaborador_lotacao (colaborador_id, cargo_id, departamento_id, gestor_id, centro_custo_id, cargo_cbo,
                                 nivel_cargo, data_inicio, data_criacao, data_alteracao)
SELECT f.id,
       c.id,
       d.id,
       f.id,
       cc.id,
       '2121',
       'SENIOR1',
       NOW(),
       NOW(),
       NOW()
FROM colaborador f
         JOIN cargo c ON c.nome = 'Desenvolvedor Backend'
         JOIN departamento d ON d.descricao = 'TI'
         JOIN centro_custo cc ON cc.nome = 'TI01'
WHERE f.matricula = 'MAT001' ON CONFLICT (colaborador_id) DO NOTHING;

-- Lotação do Venio
INSERT INTO colaborador_lotacao (colaborador_id, cargo_id, departamento_id, gestor_id, centro_custo_id, cargo_cbo,
                                 nivel_cargo, data_inicio, data_criacao, data_alteracao)
SELECT f.id,
       c.id,
       d.id,
       f_gestor.id, -- gestor do departamento
       cc.id,
       '2121',
       'JUNIOR1',
       NOW(),
       NOW(),
       NOW()
FROM colaborador f
         JOIN cargo c ON c.nome = 'Desenvolvedor Frontend'
         JOIN departamento d ON d.descricao = 'TI'
         JOIN centro_custo cc ON cc.nome = 'TI01'
         JOIN colaborador f_gestor ON f_gestor.matricula = 'MAT001'
WHERE f.matricula = 'MAT002' ON CONFLICT (colaborador_id) DO NOTHING;


-- Salários
INSERT INTO colaborador_salario (colaborador_contrato_id, valor, data_inicio, data_fim, motivo_alteracao_salario)
SELECT fc.id, 3000.00, '2025-01-01', '2025-12-31', 'ADMISSAO'
FROM colaborador_contrato fc
         JOIN colaborador f ON f.id = fc.colaborador_id
WHERE f.matricula = 'MAT001';

INSERT INTO colaborador_salario (colaborador_contrato_id, valor, data_inicio, data_fim, motivo_alteracao_salario)
SELECT fc.id, 2000.00, '2025-01-01', '2025-12-31', 'ADMISSAO'
FROM colaborador_contrato fc
         JOIN colaborador f ON f.id = fc.colaborador_id
WHERE f.matricula = 'MAT002';

INSERT INTO colaborador_endereco (colaborador_id,
                                  tipo_endereco,
                                  cep,
                                  logradouro,
                                  numero,
                                  complemento,
                                  bairro,
                                  municipio,
                                  uf,
                                  pais,
                                  status,
                                  data_criacao,
                                  data_alteracao)
SELECT c.id,
       'RESIDENCIAL',
       '01001000',
       'Praça da Sé',
       '100',
       'Apto 101',
       'Sé',
       'São Paulo',
       'SP',
       'Brasil',
       'ATIVO',
       CURRENT_DATE,
       NULL
FROM colaborador c
WHERE c.matricula = 'MAT000001';

INSERT INTO colaborador_endereco (colaborador_id,
                                  tipo_endereco,
                                  cep,
                                  logradouro,
                                  numero,
                                  complemento,
                                  bairro,
                                  municipio,
                                  uf,
                                  pais,
                                  status,
                                  data_criacao,
                                  data_alteracao)
SELECT c.id,
       'RESIDENCIAL',
       '30140071',
       'Avenida Afonso Pena',
       '1500',
       'Sala 1201',
       'Centro',
       'Belo Horizonte',
       'MG',
       'Brasil',
       'ATIVO',
       CURRENT_DATE,
       NULL
FROM colaborador c
WHERE c.matricula = 'MAT000002';

INSERT INTO colaborador_dados_bancarios (colaborador_id,
                                         banco_codigo,
                                         banco_nome,
                                         agencia,
                                         conta,
                                         digito_conta,
                                         tipo_conta,
                                         chave_pix,
                                         forma_pagamento,
                                         status,
                                         data_criacao,
                                         data_alteracao)
SELECT c.id,
       '001',
       'Banco do Brasil',
       '1234',
       '987654',
       '1',
       'CORRENTE',
       NULL,
       'TED',
       'ATIVO',
       NOW(),
       NOW()
FROM colaborador c
WHERE c.matricula = 'MAT000001' ON CONFLICT DO NOTHING;

INSERT INTO colaborador_dados_bancarios (colaborador_id,
                                         banco_codigo,
                                         banco_nome,
                                         agencia,
                                         conta,
                                         digito_conta,
                                         tipo_conta,
                                         chave_pix,
                                         forma_pagamento,
                                         status,
                                         data_criacao,
                                         data_alteracao)
SELECT c.id,
       '341',
       'Itaú Unibanco',
       '4321',
       '123456',
       '7',
       'CORRENTE',
       'venio@empresa.com',
       'PIX',
       'ATIVO',
       NOW(),
       NOW()
FROM colaborador c
WHERE c.matricula = 'MAT000002' ON CONFLICT DO NOTHING;

INSERT INTO colaborador_documentos (colaborador_id,
                                    cpf,
                                    pis_pasep,
                                    possui_ctps_fisica,
                                    ctps_numero,
                                    ctps_serie,
                                    registro_estrangeiro,
                                    data_criacao,
                                    data_alteracao,
                                    status)
SELECT c.id,
       '11122233344',
       '123456789012',
       FALSE,
       NULL,
       NULL,
       '123456789',
       NOW(),
       NULL,
       'ATIVO'
FROM colaborador c
WHERE c.matricula = 'MAT000001' ON CONFLICT (colaborador_id) DO NOTHING;


INSERT INTO colaborador_documentos (colaborador_id,
                                    cpf,
                                    pis_pasep,
                                    possui_ctps_fisica,
                                    ctps_numero,
                                    ctps_serie,
                                    registro_estrangeiro,
                                    data_criacao,
                                    data_alteracao,
                                    status)
SELECT c.id,
       '55566677788',
       '123456789013',
       TRUE,
       12231,
       1,
       '123456710',
       NOW(),
       NULL,
       'ATIVO'
FROM colaborador c
WHERE c.matricula = 'MAT000002' ON CONFLICT (colaborador_id) DO NOTHING;
