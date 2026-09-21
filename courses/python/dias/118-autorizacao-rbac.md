# Dia 118 — Autorização e RBAC

## Objetivo
Controlar o que cada identidade pode fazer.

## Teoria
RBAC associa permissões a papéis. A aplicação deve verificar autorização no ponto em que a operação é executada, não apenas esconder opções na interface.

## Prática
Crie papéis como:
- leitor;
- operador;
- administrador.

Defina permissões e teste cada combinação.

## Desafio
Adicione uma regra específica para um recurso e prove que um usuário sem permissão é bloqueado.

## Checklist
- [ ] Permissões são explícitas.
- [ ] Autorização ocorre no backend.
- [ ] Casos permitidos e negados têm testes.
