# Dia 17 — Fetch API

## Objetivo
Consumir dados HTTP no navegador.

```javascript
const resposta = await fetch('/api/tarefas');
if (!resposta.ok) throw new Error('Falha HTTP');
const dados = await resposta.json();
```

## Exercício
Consuma uma API pública de testes e mostre os dados na página.

## Desafio
Trate estados de carregamento, sucesso, resposta vazia e erro.
