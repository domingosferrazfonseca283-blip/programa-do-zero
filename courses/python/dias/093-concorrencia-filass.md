# Dia 93 — Concorrência com filas

## Objetivo
Usar filas para desacoplar produtores e consumidores.

## Teoria
Uma fila permite que uma parte do sistema produza trabalho enquanto outra processa posteriormente.

Conceitos:
- producer;
- consumer;
- capacidade;
- bloqueio;
- encerramento;
- erro no processamento.

## Prática
Crie uma fila de tarefas em Python e dois consumidores.

Registre:
- tarefa recebida;
- início;
- conclusão;
- erro.

## Desafio
Defina uma política segura para encerramento dos consumidores.

## Checklist
- [ ] Entendi producer/consumer.
- [ ] Evitei acesso concorrente inseguro.
- [ ] Tratei erros.
- [ ] Defini encerramento.
