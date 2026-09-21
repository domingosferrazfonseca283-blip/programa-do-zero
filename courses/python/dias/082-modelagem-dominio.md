# Dia 82 — Modelagem do domínio

## Objetivo
Representar conceitos do problema de forma explícita.

## Teoria
O domínio contém as regras importantes do negócio. Uma boa modelagem começa por identificar:
- entidades;
- valores;
- relações;
- invariantes;
- operações permitidas.

Não coloque toda regra dentro da interface. O domínio deve continuar compreensível mesmo se a interface mudar.

## Prática
Modele uma tarefa com:
- identificador;
- título;
- estado;
- prioridade;
- prazo.

Defina operações válidas e estados possíveis.

## Desafio
Liste cinco situações que devem ser rejeitadas pelo domínio.

## Checklist
- [ ] Identifiquei entidades.
- [ ] Defini estados.
- [ ] Defini invariantes.
- [ ] Separei regra de negócio da interface.
