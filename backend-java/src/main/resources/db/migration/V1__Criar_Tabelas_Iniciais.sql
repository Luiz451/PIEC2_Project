-- Extensão necessária para suporte de embeddings na visão computacional
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE fazenda (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         nome VARCHAR(255) NOT NULL,
                         localizacao VARCHAR(500),
                         timezone VARCHAR(50) DEFAULT 'America/Sao_Paulo',
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE usuario (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         fazenda_id UUID REFERENCES fazenda (id) ON DELETE CASCADE,
                         nome VARCHAR(255) NOT NULL,
                         email VARCHAR(255) UNIQUE NOT NULL,
                         senha_hash VARCHAR(255) NOT NULL,
                         role VARCHAR(50) DEFAULT 'operador' CHECK (role IN ('admin', 'fazendeiro', 'operador', 'veterinario')),
                         ativo BOOLEAN DEFAULT TRUE,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE TABLE animal (
                        id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                        fazenda_id UUID REFERENCES fazenda (id) ON DELETE CASCADE,
                        brinco VARCHAR(50) NOT NULL,
                        nome VARCHAR(255) NOT NULL,
                        raca VARCHAR(100) NOT NULL,
                        data_nasc DATE,
                        sexo VARCHAR(20) CHECK (sexo IN ('femea', 'macho')),
                        status VARCHAR(50) DEFAULT 'cadastrado' CHECK (status IN ('cadastrado', 'em_lactacao', 'gestante', 'seca', 'vendido', 'morto')),
                        foto_url TEXT,
                        embedding VECTOR(768),
                        created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
                        UNIQUE (fazenda_id, brinco)
);

CREATE TABLE ordenha (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         animal_id UUID REFERENCES animal (id) ON DELETE CASCADE,
                         data DATE NOT NULL,
                         turno VARCHAR(20) CHECK (turno IN ('manha', 'tarde')),
                         litros DECIMAL(6,2) NOT NULL CHECK (litros >= 0),
                         temperatura DECIMAL(4,1),
                         operador_id UUID REFERENCES usuario(id),
                         observacao TEXT,
                         created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
                         UNIQUE (animal_id, data, turno)
);