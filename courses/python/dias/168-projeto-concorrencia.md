# Dia 168 — Projeto: pipeline concorrente

## Objetivo
Construir um pipeline que combine I/O, filas e processamento.

## Cenário
Crie um pipeline local:
1. produtor gera tarefas;
2. consumidores realizam I/O simulado;
3. resultados entram em outra fila;
4. etapa final agrega os resultados.

## Requisitos
- limite de concorrência;
- cancelamento;
- timeout;
- retry limitado;
- logs;
- métricas;
- testes.

## Critério
O pipeline deve terminar corretamente tanto em execução normal quanto diante de falhas controladas.

## Checklist
- [ ] Concorrência limitada.
- [ ] Cancelamento seguro.
- [ ] Falhas tratadas.
- [ ] Resultados reproduzíveis.
