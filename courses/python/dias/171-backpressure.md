# Dia 171 — Backpressure

## Objetivo
Evitar que produtores gerem trabalho mais rápido do que consumidores conseguem processar.

## Conceitos
- fila ilimitada versus limitada;
- pressão de memória;
- throughput;
- latência;
- bloqueio do produtor;
- rejeição controlada.

## Prática guiada
Monte um produtor que gera tarefas rapidamente e consumidores mais lentos. Compare uma fila ilimitada com uma fila assíncrona de capacidade máxima.

Meça tamanho da fila, tempo de espera, quantidade processada e quantidade rejeitada.

## Desafio
Implemente uma política explícita para fila cheia: esperar, rejeitar ou descartar apenas tarefas classificadas como não essenciais.

## Checklist
- [ ] Sei explicar backpressure.
- [ ] Sei por que fila ilimitada pode mascarar sobrecarga.
- [ ] Medi latência e throughput.
- [ ] Tenho política definida para saturação.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
