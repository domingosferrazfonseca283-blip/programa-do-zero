# Dia 13 — Context e cancelamento

## Objetivo
Aprender a controlar operações concorrentes.

## Conceito
`context.Context` é usado para deadlines, cancelamento e propagação de sinais entre operações.

```go
ctx, cancel := context.WithCancel(context.Background())
defer cancel()
```

## Exercício
Crie uma tarefa que verifica `ctx.Done()` e encerra quando receber cancelamento.

## Desafio
Simule uma operação longa com timeout e trate o cancelamento corretamente.

## Checklist
- [ ] entendo cancelamento
- [ ] sei usar timeout
- [ ] não ignoro erros de contexto
