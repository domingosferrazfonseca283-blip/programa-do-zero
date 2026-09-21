# Dia 107 — Entidades e invariantes

## Objetivo
Colocar regras importantes no lugar correto.

## Teoria
Uma entidade deve proteger invariantes do domínio. Nem toda validação pertence à interface.

## Prática
Modele uma entidade que não possa assumir estados inválidos.

Exemplos de invariantes:
- quantidade não negativa;
- estado permitido;
- identificador obrigatório;
- transição de estado válida.

## Desafio
Crie testes que tentem violar cada invariante.

## Checklist
- [ ] Identifiquei invariantes.
- [ ] O domínio protege estados inválidos.
- [ ] Testei transições válidas e inválidas.
