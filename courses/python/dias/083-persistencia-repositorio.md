# Dia 83 — Persistência e Repository

## Objetivo
Separar armazenamento das regras de negócio.

## Teoria
O código de domínio não precisa conhecer SQL diretamente. Um repository pode oferecer operações como:
- guardar;
- procurar;
- listar;
- atualizar;
- remover.

A implementação pode usar SQLite hoje e outra tecnologia no futuro.

## Prática
Crie uma interface conceitual de repository para tarefas e uma implementação SQLite.

Teste o repository separadamente.

## Desafio
Crie uma implementação em memória para testes rápidos.

## Checklist
- [ ] Separei domínio e persistência.
- [ ] Usei consultas parametrizadas.
- [ ] Testei operações do repository.
- [ ] Consigo substituir SQLite por armazenamento em memória.
