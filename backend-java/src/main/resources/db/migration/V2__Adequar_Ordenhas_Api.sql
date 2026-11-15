-- Mantém a V1 publicada na main e adapta registros existentes ao contrato do CRUD.
ALTER TABLE animal ADD CONSTRAINT uk_animal_id_fazenda UNIQUE (id, fazenda_id);

ALTER TABLE ordenha ADD COLUMN fazenda_id UUID;
UPDATE ordenha o SET fazenda_id = a.fazenda_id FROM animal a WHERE a.id = o.animal_id;
ALTER TABLE ordenha ALTER COLUMN fazenda_id SET NOT NULL;
ALTER TABLE ordenha ALTER COLUMN animal_id SET NOT NULL;

ALTER TABLE ordenha ADD COLUMN data_ordenha DATE;
UPDATE ordenha SET data_ordenha = data;
ALTER TABLE ordenha ALTER COLUMN data_ordenha SET NOT NULL;

ALTER TABLE ordenha ADD COLUMN hora_ordenha TIME NOT NULL DEFAULT TIME '00:00:00';
ALTER TABLE ordenha ADD COLUMN volume_litros NUMERIC(10, 3);
UPDATE ordenha SET volume_litros = litros;
ALTER TABLE ordenha ALTER COLUMN volume_litros SET NOT NULL;
ALTER TABLE ordenha ADD CONSTRAINT ck_ordenha_volume_positivo CHECK (volume_litros >= 0);

ALTER TABLE ordenha ALTER COLUMN turno TYPE VARCHAR(40);
ALTER TABLE ordenha DROP CONSTRAINT IF EXISTS ordenha_turno_check;
UPDATE ordenha SET turno = UPPER(TRIM(turno));
ALTER TABLE ordenha ALTER COLUMN turno SET NOT NULL;
ALTER TABLE ordenha ADD CONSTRAINT ck_ordenha_turno CHECK (turno IN ('MANHA', 'TARDE'));

ALTER TABLE ordenha ADD COLUMN criado_em TIMESTAMP WITH TIME ZONE;
UPDATE ordenha SET criado_em = COALESCE(created_at, CURRENT_TIMESTAMP);
ALTER TABLE ordenha ALTER COLUMN criado_em SET NOT NULL;
ALTER TABLE ordenha ADD COLUMN atualizado_em TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE ordenha DROP COLUMN data;
ALTER TABLE ordenha DROP COLUMN litros;
ALTER TABLE ordenha ADD CONSTRAINT uk_ordenha_animal_turno UNIQUE (fazenda_id, animal_id, data_ordenha, turno);
ALTER TABLE ordenha ADD CONSTRAINT fk_ordenha_animal_fazenda
    FOREIGN KEY (animal_id, fazenda_id) REFERENCES animal (id, fazenda_id);

CREATE INDEX idx_ordenha_fazenda_data ON ordenha (fazenda_id, data_ordenha);
CREATE INDEX idx_ordenha_animal_data ON ordenha (animal_id, data_ordenha);
