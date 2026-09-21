# Dia 105 — Arquitetura hexagonal

## Objetivo
Separar regras de negócio das tecnologias externas.

## Teoria
A arquitetura hexagonal organiza o sistema em torno do domínio. Entradas e saídas passam por portas (interfaces), enquanto adaptadores conectam HTTP, CLI, banco e outros recursos.

## Prática
Desenhe:
CLI/HTTP → porta → aplicação → domínio → porta → banco

## Desafio
Troque o adaptador de persistência sem alterar a regra de negócio.

## Checklist
- [ ] Domínio não depende de HTTP.
- [ ] Domínio não depende do banco.
- [ ] Portas têm responsabilidades claras.
- [ ] Adaptadores podem ser substituídos.
