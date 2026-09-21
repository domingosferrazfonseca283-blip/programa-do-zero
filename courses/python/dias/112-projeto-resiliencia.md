# Dia 112 — Projeto: serviço resiliente

## Objetivo
Aplicar arquitetura e resiliência em um único exercício.

## Cenário
Crie um serviço que consulta uma dependência externa simulada.

O sistema deve possuir:
- timeout;
- retry limitado;
- backoff;
- circuit breaker;
- logs;
- métricas;
- testes.

## Regras
A dependência externa deve ser simulada. Não use serviços reais para testar falhas.

## Entrega
Documente:
1. arquitetura;
2. política de retries;
3. estados do circuito;
4. comportamento em falhas;
5. testes;
6. métricas escolhidas.

## Checklist
- [ ] Falha temporária é tratada.
- [ ] Falha persistente não gera retry infinito.
- [ ] O sistema é observável.
- [ ] Testes cobrem os cenários críticos.
