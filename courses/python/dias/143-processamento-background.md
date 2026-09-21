# Dia 143 — Jobs em background

## Objetivo
Separar operações demoradas do caminho síncrono quando isso fizer sentido.

## Conceitos
- job;
- worker;
- fila;
- retry;
- status;
- idempotência.

## Prática
Modele um job de processamento de relatório.

O usuário deve conseguir consultar o estado do job sem esperar sua conclusão.

## Desafio
Defina o comportamento para falha, repetição e cancelamento.

## Checklist
- [ ] Job tem identificador.
- [ ] Estado é consultável.
- [ ] Falhas são tratadas.
- [ ] Repetição é segura.
