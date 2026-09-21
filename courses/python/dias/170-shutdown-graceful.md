# Dia 170 — Encerramento gracioso

## Objetivo
Construir programas concorrentes que terminam sem abandonar tarefas, arquivos, conexões ou filas em estado inconsistente.

## Conceitos
- sinalização de parada;
- cancelamento cooperativo;
- CancelledError;
- limpeza em finally;
- encerramento de workers;
- timeout de shutdown.

## Prática guiada
Crie uma fila assíncrona com três workers. Ao receber uma ordem de parada, o produtor deixa de aceitar tarefas, os workers drenam a fila e o programa encerra recursos.

## Desafio
Adicione um prazo máximo de shutdown e registre quais tarefas não terminaram, sem esconder o erro.

## Checklist
- [ ] Sei cancelar cooperativamente.
- [ ] Uso finally para limpeza.
- [ ] Diferencio shutdown de cancelamento abrupto.
- [ ] Tenho testes para falhas durante o encerramento.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
