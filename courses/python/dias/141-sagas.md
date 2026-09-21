# Dia 141 — Sagas e processos distribuídos

## Objetivo
Conhecer estratégias para processos que atravessam múltiplos componentes.

## Teoria
Uma saga coordena etapas que podem possuir compensações quando uma etapa posterior falha.

## Prática
Modele um processo:
pedido → reserva → pagamento → confirmação.

Defina o que acontece se cada etapa falhar.

## Desafio
Especifique ações compensatórias sem assumir que uma transação global existe.

## Checklist
- [ ] Etapas estão explícitas.
- [ ] Falhas foram consideradas.
- [ ] Compensações estão documentadas.
