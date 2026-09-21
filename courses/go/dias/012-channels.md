# Dia 12 — Channels

## Objetivo
Aprender a comunicação entre goroutines.

## Conceito
Channels permitem enviar e receber valores entre goroutines.

```go
ch := make(chan string)
go func() { ch <- "olá" }()
mensagem := <-ch
```

## Exercício
Crie um produtor que envie cinco números para um channel e um consumidor que os leia.

## Desafio
Monte um pipeline simples: produtor → processamento → consumidor.

## Reflexão
Pergunte: este problema realmente precisa de concorrência? Concorrência deve resolver uma necessidade, não ser usada apenas porque a linguagem oferece o recurso.
