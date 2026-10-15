# Plano inicial do CRUD de ordenhas

Este plano detalha a [issue #5](https://github.com/Luiz451/PIEC2_Project/issues/5). A primeira implementação do CRUD foi iniciada no backend seguindo este contrato.

## Objetivo

Permitir registrar, consultar, corrigir e excluir a produção de leite de um animal em uma ordenha. Os registros deverão alimentar os indicadores de produção do ERP. Produção de leite e venda de leite são fatos diferentes: cadastrar uma ordenha não deve gerar receita financeira automaticamente.

## Modelo mínimo proposto

Uma `Ordenha` deve ter identificador próprio, referência à fazenda e ao animal, data, hora, turno, volume em litros e horário de criação/alteração do registro. O volume deve usar `BigDecimal` no Java e tipo decimal no PostgreSQL, com valor maior que zero. A referência ao animal deve existir e pertencer à fazenda informada.

Regra definida pelo usuário: cada animal pode ter somente uma ordenha por turno em cada data. O turno será recebido como identificador textual para acomodar os horários definidos por cada propriedade, removendo espaços nas pontas e ignorando diferenças entre maiúsculas e minúsculas. A combinação fazenda, animal, data e turno deve ser única. A restrição existe também no banco para proteger contra gravações simultâneas e sincronização offline futura.

## API proposta

Seguindo a versão e o estilo da rota de animais, criar:

- `POST /api/v1/ordenhas` — cadastrar uma ordenha.
- `GET /api/v1/ordenhas` — listar com filtros por fazenda, animal e intervalo de datas, com paginação.
- `GET /api/v1/ordenhas/{id}` — consultar um registro.
- `PUT /api/v1/ordenhas/{id}` — corrigir um registro.
- `DELETE /api/v1/ordenhas/{id}` — excluir um registro.

Usar DTOs de entrada e saída, validação de campos e respostas claras para entrada inválida (`400`), animal ou ordenha inexistente (`404`) e duplicidade conforme a regra definida (`409`). O identificador da fazenda recebido na requisição não deve substituir uma futura verificação de acesso por usuário; a autorização depende da [issue #12](https://github.com/Luiz451/PIEC2_Project/issues/12).

Os endpoints de listagem, consulta, edição e remoção recebem `fazendaId` como parâmetro de consulta. A listagem aceita também `animalId`, `dataInicial`, `dataFinal`, `page` e `size` (padrão de 20 e limite de 100). O corpo de criação e edição contém `fazendaId`, `animalId`, `dataOrdenha` (`AAAA-MM-DD`), `horaOrdenha` (`HH:mm:ss`), `turno` e `volumeLitros`.

## Sequência de implementação

1. Regra definida: uma ordenha por animal, fazenda, data e turno. A autorização por usuário depende da [issue #12](https://github.com/Luiz451/PIEC2_Project/issues/12).
2. Migration inicial, entidade, repositório, serviço, DTOs, controller paginado e respostas de erro foram criados. A migration inicia a persistência de animais e ordenhas prevista na [issue #2](https://github.com/Luiz451/PIEC2_Project/issues/2).
3. Validar a implementação com PostgreSQL e ampliar a cobertura para leitura, atualização, exclusão, filtros e restrição de unicidade no banco quando o ambiente da [issue #4](https://github.com/Luiz451/PIEC2_Project/issues/4) estiver disponível.
4. Integrar os dados aos indicadores da [issue #6](https://github.com/Luiz451/PIEC2_Project/issues/6) em etapa própria e definir a sincronização offline junto à [issue #11](https://github.com/Luiz451/PIEC2_Project/issues/11).

## Critérios de aceite propostos

- Uma ordenha válida pode ser criada, consultada, listada, corrigida e excluída por API.
- Volume nulo, zero ou negativo e referências inválidas são rejeitados.
- Uma ordenha não pode ser vinculada a animal de outra fazenda.
- Os filtros por animal e período retornam apenas os registros correspondentes, com paginação estável.
- Erros de entrada, ausência de registro e duplicidade têm respostas HTTP verificáveis.
- Os testes cobrem os fluxos principais e as restrições de persistência definidas.

## Definições pendentes

1. O turno é um identificador textual informado pela propriedade; padronização de horários e nomes de turnos poderá ser configurada em outra etapa.
2. A exclusão será física nesta primeira versão. Auditoria e cancelamento lógico podem ser adicionados quando houver requisito.
3. O primeiro recorte guarda o volume. Observações e destino do leite ficam fora desta implementação inicial.

O contrato inicial já usa esses critérios. A configuração de turnos por propriedade, auditoria e integração com sincronização offline ficam para etapas posteriores, caso os requisitos sejam confirmados.
