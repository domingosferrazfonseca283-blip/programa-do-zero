# Dia 137 — Agregados e consistência

## Objetivo
Definir fronteiras de consistência no domínio.

## Teoria
Um agregado agrupa objetos que precisam obedecer a invariantes em conjunto. O agregado deve ter uma raiz responsável por controlar suas alterações.

## Prática
Escolha o domínio do projeto e identifique:
- raiz;
- entidades internas;
- invariantes;
- operações permitidas.

## Desafio
Explique quais alterações devem ocorrer dentro de uma única transação.

## Checklist
- [ ] Identifiquei raízes de agregado.
- [ ] Invariantes estão protegidas.
- [ ] Evitei agregados excessivamente grandes.
