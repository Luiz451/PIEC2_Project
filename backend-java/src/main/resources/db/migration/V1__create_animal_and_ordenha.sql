CREATE TABLE IF NOT EXISTS animal (
    id UUID PRIMARY KEY,
    fazenda_id UUID,
    brinco VARCHAR(50) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    raca VARCHAR(100) NOT NULL,
    data_nasc DATE,
    sexo VARCHAR(20),
    status VARCHAR(50),
    foto_url TEXT,
    CONSTRAINT uk_animal_id_fazenda UNIQUE (id, fazenda_id)
);

CREATE TABLE ordenha (
    id UUID PRIMARY KEY,
    fazenda_id UUID NOT NULL,
    animal_id UUID NOT NULL,
    data_ordenha DATE NOT NULL,
    hora_ordenha TIME NOT NULL,
    turno VARCHAR(40) NOT NULL,
    volume_litros NUMERIC(10, 3) NOT NULL,
    criado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_ordenha_volume_positivo CHECK (volume_litros > 0),
    CONSTRAINT uk_ordenha_animal_turno UNIQUE (fazenda_id, animal_id, data_ordenha, turno),
    CONSTRAINT fk_ordenha_animal_fazenda
        FOREIGN KEY (animal_id, fazenda_id) REFERENCES animal (id, fazenda_id)
);

CREATE INDEX idx_ordenha_fazenda_data ON ordenha (fazenda_id, data_ordenha);
CREATE INDEX idx_ordenha_animal_data ON ordenha (animal_id, data_ordenha);
