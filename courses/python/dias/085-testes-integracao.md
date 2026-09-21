# Dia 85 — Testes de integração

## Objetivo
Verificar se componentes reais funcionam juntos.

## Teoria
Teste unitário verifica uma unidade isolada. Teste de integração verifica a interação entre componentes reais.

Exemplo:
CLI/API → serviço → repository → SQLite.

## Prática
Crie testes que:
- criem uma tarefa;
- persistam a tarefa;
- recuperem a tarefa;
- alterem o estado;
- verifiquem a persistência.

Use uma base temporária.

## Desafio
Teste também falhas de integridade e dados inválidos.

## Checklist
- [ ] Tenho testes de integração.
- [ ] Os testes usam ambiente isolado.
- [ ] Testei persistência real.
- [ ] Testei caminhos de erro.
