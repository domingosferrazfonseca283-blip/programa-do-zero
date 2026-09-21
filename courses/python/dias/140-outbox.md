# Dia 140 — Outbox Pattern

## Objetivo
Entender como persistir uma alteração de negócio e um evento de forma coordenada.

## Teoria
O padrão Outbox grava o evento em uma estrutura persistente dentro da mesma transação da alteração principal. Um processo posterior publica o evento.

## Prática
Modele:
1. alteração de negócio;
2. registro na outbox;
3. processamento posterior.

## Desafio
Defina como tratar uma mensagem processada novamente.

## Checklist
- [ ] Entendi o problema de publicação inconsistente.
- [ ] Evento fica persistido.
- [ ] Duplicação é considerada.
