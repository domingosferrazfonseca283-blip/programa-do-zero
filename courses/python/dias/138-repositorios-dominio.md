# Dia 138 — Repositories no domínio

## Objetivo
Separar a necessidade do domínio de persistência de sua implementação.

## Prática
Defina uma abstração de repository orientada às necessidades do domínio.

Depois crie uma implementação em memória para testes.

## Desafio
Crie uma implementação persistente sem alterar os casos de uso.

## Checklist
- [ ] Interface representa necessidade do domínio.
- [ ] Infraestrutura implementa a interface.
- [ ] Testes podem usar implementação em memória.
