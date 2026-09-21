# Dia 98 — Tracing e fluxo de requisições

## Objetivo
Acompanhar uma operação através de diferentes componentes.

## Teoria
Um trace representa uma operação distribuída. Spans ajudam a identificar onde o tempo foi gasto.

## Prática
Modele uma requisição:
`HTTP → serviço → repository → banco`

Registre tempos e identificadores de correlação.

## Desafio
Analise uma requisição lenta e identifique o componente responsável pelo maior tempo.

## Checklist
- [ ] Entendi trace e span.
- [ ] Consigo seguir uma requisição.
- [ ] Evitei registrar dados sensíveis.
