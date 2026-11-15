INSERT INTO fazenda (id, nome, localizacao, timezone)
VALUES (
           '00000000-0000-0000-0000-000000000001',
           'Fazenda Experimental UFRPE - UABJ',
           'Belo Jardim - PE',
           'America/Recife'
       )
    ON CONFLICT (id) DO NOTHING;