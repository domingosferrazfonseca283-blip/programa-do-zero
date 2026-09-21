# Dia 11 — Concorrência com goroutines

## Objetivo
Entender como o Go executa trabalho concorrente com goroutines.

## Conceito
Uma goroutine é uma função executada concorrentemente pelo runtime do Go.

```go
go processar()
```

O objetivo inicial não é criar o máximo de goroutines, mas compreender quando a concorrência simplifica um problema.

## Exercício
1. Crie uma função que imprime mensagens.
2. Execute-a normalmente.
3. Execute-a como goroutine.
4. Observe que o programa pode terminar antes da goroutine concluir.
5. Use `sync.WaitGroup` para coordenar a conclusão.

## Desafio
Crie um programa que processe dez tarefas e aguarde todas terminarem.

## Checklist
- [ ] sei criar uma goroutine
- [ ] entendo o problema de encerramento prematuro
- [ ] sei usar WaitGroup
