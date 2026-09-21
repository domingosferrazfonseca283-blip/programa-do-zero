# Dia 89 — Arquitetura e dependências

## Objetivo
Controlar dependências entre componentes para reduzir acoplamento.

## Teoria
Uma dependência existe quando um componente precisa conhecer detalhes de outro. Arquiteturas mais fáceis de evoluir mantêm regras centrais independentes de interfaces e infraestrutura.

## Prática
Desenhe o fluxo:
`interface → serviço → domínio → repository`

Identifique quem conhece quem e quais dependências podem ser invertidas.

## Desafio
Substitua uma dependência concreta por uma abstração simples e teste o serviço com uma implementação falsa.

## Checklist
- [ ] Identifiquei dependências.
- [ ] Reduzi dependências desnecessárias.
- [ ] Separei regra e infraestrutura.
- [ ] Testei com uma dependência substituta.
