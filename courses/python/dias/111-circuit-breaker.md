# Dia 111 — Circuit breaker

## Objetivo
Conhecer um padrão para impedir chamadas repetidas a uma dependência indisponível.

## Estados
- fechado;
- aberto;
- half-open.

## Prática
Modele um circuito que interrompa chamadas depois de uma quantidade definida de falhas e permita uma tentativa de recuperação.

## Desafio
Teste todas as transições de estado.

## Checklist
- [ ] Estados estão definidos.
- [ ] Falhas consecutivas são contabilizadas.
- [ ] Existe recuperação controlada.
