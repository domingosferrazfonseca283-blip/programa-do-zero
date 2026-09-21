# Dia 139 — Unit of Work

## Objetivo
Coordenar alterações relacionadas dentro de uma fronteira transacional.

## Conceitos
- unidade de trabalho;
- commit;
- rollback;
- consistência.

## Prática
Modele uma operação que altera múltiplos repositories e precisa ser confirmada como uma única operação.

## Desafio
Simule uma falha no meio da operação e prove que o rollback preserva a consistência.

## Checklist
- [ ] Fronteira transacional está clara.
- [ ] Commit é explícito.
- [ ] Rollback é testado.
