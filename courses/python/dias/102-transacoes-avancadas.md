# Dia 102 — Transações e consistência

## Objetivo
Raciocinar sobre operações que precisam ser atômicas.

## Conceitos
- atomicidade;
- commit;
- rollback;
- isolamento;
- consistência;
- concorrência.

## Prática
Implemente uma operação que altera duas entidades e garanta que ambas sejam persistidas ou nenhuma seja.

## Desafio
Simule uma falha entre duas etapas e prove que o estado final continua consistente.

## Checklist
- [ ] Identifiquei a fronteira transacional.
- [ ] Testei rollback.
- [ ] Considerei concorrência.
