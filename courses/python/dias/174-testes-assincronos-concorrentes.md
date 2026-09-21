# Dia 174 — Testes assíncronos e concorrentes

## Objetivo
Testar código concorrente sem depender de sorte ou de sleep arbitrário.

## Conceitos
- testes assíncronos;
- eventos e barreiras;
- determinismo;
- fixtures;
- sincronização controlada;
- testes de cancelamento.

## Prática guiada
Escreva testes para uma fila de workers usando eventos explícitos para controlar quando cada worker avança.

## Evite
- sleep como mecanismo de sincronização;
- depender da ordem acidental das tasks;
- testar concorrência apenas em uma execução.

## Desafio
Crie um teste que reproduza uma condição de corrida através de pontos de sincronização controlados.

## Checklist
- [ ] Meus testes são determinísticos.
- [ ] Sei controlar a ordem quando necessário.
- [ ] Testo cancelamento.
- [ ] Testo falhas simultâneas.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
