# Dia 176 — Projeto: serviço resiliente

## Objetivo
Integrar concorrência, filas, limites, cancelamento, observabilidade e testes em um único projeto local.

## Requisitos
Construa um serviço que receba tarefas e as processe em background.

O projeto deve possuir:
- API local;
- fila limitada;
- concorrência configurável;
- timeout por tarefa;
- retry apenas quando apropriado;
- backpressure;
- shutdown gracioso;
- logs estruturados;
- métricas básicas;
- testes unitários e de integração;
- configuração por ambiente;
- documentação de execução.

## Arquitetura sugerida
Cliente -> API -> Validação -> Fila -> Workers -> Serviço/DB

## Critérios de qualidade
1. O limite de concorrência deve ser verificável por teste.
2. O shutdown deve impedir novas entradas e finalizar o trabalho aceito dentro do prazo.
3. A fila deve ter capacidade máxima.
4. Falhas temporárias devem seguir política explícita.
5. Logs não devem expor segredos.
6. O projeto deve rodar localmente de forma reproduzível.

## Entregáveis
- código;
- testes;
- README;
- desenho da arquitetura;
- tabela de decisões;
- relatório de testes de carga local;
- limitações conhecidas.

## Desafio final
Introduza falhas controladas no laboratório e demonstre que o serviço continua previsível sob saturação, timeout, erro de dependência e shutdown.

## Checklist
- [ ] Backpressure.
- [ ] Concorrência limitada.
- [ ] Cancelamento.
- [ ] Shutdown.
- [ ] Retries seguros.
- [ ] Observabilidade.
- [ ] Testes.
- [ ] Documentação.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
