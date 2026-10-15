# Plano inicial do CRUD de ordenhas

Este plano detalha a [issue #5](https://github.com/Luiz451/PIEC2_Project/issues/5) para o backend atual. É uma proposta de implementação; nenhum código de ordenhas foi criado ainda.

## Objetivo

Permitir registrar, consultar, corrigir e excluir a produção de leite de um animal em uma ordenha. Os registros deverão alimentar os indicadores de produção do ERP. Produção de leite e venda de leite são fatos diferentes: cadastrar uma ordenha não deve gerar receita financeira automaticamente.

## Modelo mínimo proposto

Uma `Ordenha` deve ter identificador próprio, referência à fazenda e ao animal, data e hora da coleta, volume em litros e horário de criação/alteração do registro. O volume deve usar `BigDecimal` no Java e tipo decimal no PostgreSQL, com valor maior que zero. A referência ao animal deve existir e pertencer à fazenda informada.

O turno da ordenha (por exemplo, manhã ou tarde) pode ser necessário para impedir lançamentos duplicados e produzir relatórios. Antes de fixar a restrição de unicidade no banco, é preciso definir se o produto permite mais de uma ordenha do mesmo animal no mesmo turno e como tratar registros feitos offline que chegam depois.

## API proposta

Seguindo a versão e o estilo da rota de animais, criar:

- `POST /api/v1/ordenhas` — cadastrar uma ordenha.
- `GET /api/v1/ordenhas` — listar com filtros por fazenda, animal e intervalo de datas, com paginação.
- `GET /api/v1/ordenhas/{id}` — consultar um registro.
- `PUT /api/v1/ordenhas/{id}` — corrigir um registro.
- `DELETE /api/v1/ordenhas/{id}` — excluir um registro.

Usar DTOs de entrada e saída, validação de campos e respostas claras para entrada inválida (`400`), animal ou ordenha inexistente (`404`) e duplicidade conforme a regra definida (`409`). O identificador da fazenda recebido na requisição não deve substituir uma futura verificação de acesso por usuário; a autorização depende da [issue #12](https://github.com/Luiz451/PIEC2_Project/issues/12).

## Sequência de implementação

1. Definir a regra de negócio de duplicidade e o contrato de identificação da fazenda. Especificar também como correções e exclusões afetarão indicadores históricos.
2. Preparar schema e migration da tabela de ordenhas, com chaves para animal e fazenda, índices para as consultas por animal e data e restrições coerentes com as regras definidas. Essa etapa depende da base de persistência da [issue #2](https://github.com/Luiz451/PIEC2_Project/issues/2).
3. Criar entidade, repositório e serviço de ordenhas. Validar volume, data, existência do animal e pertencimento à fazenda antes de gravar.
4. Criar controller e DTOs. Manter a lógica de negócio no serviço e não aceitar entidades JPA diretamente como contrato público da API.
5. Testar cadastro, leitura, atualização, exclusão, filtros e erros de validação. Incluir teste de duplicidade depois que a regra estiver definida e teste de persistência com PostgreSQL quando o ambiente da [issue #4](https://github.com/Luiz451/PIEC2_Project/issues/4) estiver disponível.
6. Integrar os dados aos indicadores da [issue #6](https://github.com/Luiz451/PIEC2_Project/issues/6) em etapa própria. Definir a sincronização offline junto à [issue #11](https://github.com/Luiz451/PIEC2_Project/issues/11).

## Critérios de aceite propostos

- Uma ordenha válida pode ser criada, consultada, listada, corrigida e excluída por API.
- Volume nulo, zero ou negativo e referências inválidas são rejeitados.
- Uma ordenha não pode ser vinculada a animal de outra fazenda.
- Os filtros por animal e período retornam apenas os registros correspondentes, com paginação estável.
- Erros de entrada, ausência de registro e duplicidade têm respostas HTTP verificáveis.
- Os testes cobrem os fluxos principais e as restrições de persistência definidas.

## Definições pendentes

1. Uma ordenha corresponde a **um animal por turno**, ou pode haver múltiplas coletas do mesmo animal no mesmo turno?
2. O turno será um campo obrigatório, derivado do horário ou dispensável? Há horários oficiais da fazenda?
3. A exclusão deve apagar o registro ou preservá-lo para auditoria com cancelamento lógico?
4. O primeiro recorte precisa registrar apenas volume, ou também observações e destino do leite?

Essas decisões afetam o schema, a validação de duplicidade e o comportamento do modo offline. Devem ser confirmadas antes da implementação da migration e das rotas.
