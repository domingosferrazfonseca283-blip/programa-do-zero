# Dia 172 — Concorrência limitada

## Objetivo
Aprender a limitar trabalho simultâneo para proteger CPU, memória, banco e serviços externos.

## Conceitos
- semaphore;
- worker pool;
- limite por recurso;
- fairness;
- throughput versus contenção.

## Prática guiada
Implemente um processador assíncrono com um semáforo. Registre o maior número de tarefas simultâneas e prove que nunca ultrapassa o limite.

## Exercício
Compare limites 1, 2, 4 e 8. Não escolha pelo palpite: meça.

## Desafio
Crie limites independentes para operações de banco e chamadas HTTP simuladas.

## Checklist
- [ ] Sei aplicar um limite de concorrência.
- [ ] Sei medir concorrência real.
- [ ] Entendo contenção.
- [ ] Escolho limites por medição.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
