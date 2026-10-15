# Estado atual do projeto

Levantamento feito em 15 de outubro de 2026 a partir do código deste repositório e das issues abertas no GitHub. Este documento registra o estágio de implementação; a apresentação do produto está no [README](../README.md).

## Código disponível

O repositório contém `backend-java/`. O `pom.xml` declara Java 21, Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA, Spring Security, validação Bean Validation, Flyway e o driver PostgreSQL. Há uma entidade `Animal`, um repositório JPA, um serviço e um controller com rotas previstas em `/api/v1/animals`. Também foi iniciada a implementação do CRUD de ordenhas em `/api/v1/ordenhas`, com migration inicial e validação da regra de uma ordenha por animal, fazenda, data e turno.

O CRUD de animais ainda tem uma dependência `AnimalService` que não é inicializada no controller. O CRUD de ordenhas depende de PostgreSQL para execução normal, com conexão configurada em `application.yaml`; o contexto de teste usa H2 e aplica a migration. Não foram encontrados frontend, módulo financeiro ou código de visão computacional e IA. A autenticação própria ainda está pendente; Spring Security mantém sua configuração padrão de desenvolvimento.

Verificação em 15 de outubro de 2026: `mvn test` passou com quatro testes, incluindo inicialização do contexto e três testes unitários para cadastro, duplicidade e vínculo do animal à fazenda. Os testes usam H2 e mocks; não substituem validação de integração em PostgreSQL nem teste HTTP ponta a ponta.

## Prioridade de produto

Conforme a direção definida para o projeto, o primeiro recorte do ERP inclui **rebanho, produção leiteira e gestão financeira**. Os materiais de negócio citam gestão de caixa, vendas, preços, receita e lucratividade. Ainda é necessário transformar essa parte financeira em requisitos e tarefas específicas: nas issues abertas consultadas, não há uma issue dedicada ao módulo financeiro.

O funcionamento offline no curral é um requisito de produto. Sua arquitetura está em estudo na [issue #11](https://github.com/Luiz451/PIEC2_Project/issues/11); não há sincronização offline implementada no código atual.

## Issues abertas consultadas

### Base do ERP e entrega

- [#2 — Schemas e migrations no PostgreSQL](https://github.com/Luiz451/PIEC2_Project/issues/2)
- [#3 — Tela de gerenciamento de animais](https://github.com/Luiz451/PIEC2_Project/issues/3)
- [#4 — Ambiente local com Docker e Docker Compose](https://github.com/Luiz451/PIEC2_Project/issues/4)
- [#5 — CRUD de ordenhas](https://github.com/Luiz451/PIEC2_Project/issues/5)
- [#6 — Dashboard básico](https://github.com/Luiz451/PIEC2_Project/issues/6), com leite produzido no dia, total de animais, média por vaca e gráfico dos últimos 14 dias
- [#7 — Pipeline básico de CI/CD](https://github.com/Luiz451/PIEC2_Project/issues/7)
- [#11 — Mapeamento da arquitetura offline da PWA](https://github.com/Luiz451/PIEC2_Project/issues/11)
- [#12 — Autenticação com JWT e controle de acesso](https://github.com/Luiz451/PIEC2_Project/issues/12)

### Visão computacional e IA

- [#8 — Coleta inicial de imagens de bovinos](https://github.com/Luiz451/PIEC2_Project/issues/8)
- [#9 — Setup do pipeline YOLOv8-nano](https://github.com/Luiz451/PIEC2_Project/issues/9)
- [#10 — Estudo da integração do CLIP para embeddings](https://github.com/Luiz451/PIEC2_Project/issues/10)

## Fontes de produto

O *Business Case – Fazenda Digital* em apresentação e PDF descreve a gestão de rebanho, produção e finanças, além de operação offline, visão computacional e previsões. O *Lean Canvas Fazenda Digital* enfatiza pequenos produtores, monitoramento de baixo custo e funcionamento no curral. Tecnologias e metas numéricas citadas nesses materiais são propostas; não foram tratadas aqui como resultados medidos ou recursos entregues.
