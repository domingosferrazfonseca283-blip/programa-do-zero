# Dia 114 — Deduplicação e operações repetidas

## Objetivo
Evitar efeitos duplicados quando uma mensagem ou requisição é processada mais de uma vez.

## Teoria
Sistemas distribuídos podem entregar a mesma mensagem novamente. O consumidor deve decidir como reconhecer e tratar duplicações.

## Prática
Crie uma tabela ou estrutura de chaves processadas e faça o processamento ser seguro contra repetição.

## Desafio
Teste duas entregas iguais e prove que o efeito de negócio acontece apenas uma vez.

## Checklist
- [ ] Existe identificador da operação.
- [ ] Duplicações são detectadas.
- [ ] O comportamento é testado.
