# Estado atual do projeto

Levantamento feito em 15 de outubro de 2026 a partir do código deste repositório e das issues abertas no GitHub. Este documento registra o estágio de implementação; a apresentação do produto está no [README](../README.md).

## Código disponível

O repositório contém apenas `backend-java/`. O `pom.xml` declara Java 21, Spring Boot 4.1.1, Spring Web MVC, Spring Data JPA, Spring Security e o driver PostgreSQL. Há uma entidade `Animal`, um repositório JPA, um serviço e um controller com rotas previstas em `/api/v1/animals` para cadastro, listagem, consulta, atualização e remoção.

Essa estrutura ainda não forma um fluxo funcional completo: `AnimalController` declara `AnimalService`, mas não o inicializa; `application.yaml` contém apenas o nome da aplicação; não há configuração de banco, migrations ou configuração própria de autenticação. O teste existente verifica somente o carregamento do contexto do Spring. Não foram encontrados frontend, módulo financeiro, módulo de ordenhas nem código de visão computacional ou IA.

Não foi feita execução funcional da aplicação neste levantamento. A presença das classes e rotas indica trabalho iniciado, não uma funcionalidade validada.

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
