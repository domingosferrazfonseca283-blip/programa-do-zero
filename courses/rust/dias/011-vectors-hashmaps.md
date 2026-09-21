# Dia 11 — Vec e HashMap

## Objetivo
Aprofundar coleções essenciais de Rust.

## Vec
`Vec<T>` representa uma coleção dinâmica contígua.

```rust
let mut numeros = Vec::new();
numeros.push(10);
numeros.push(20);
```

## HashMap
Usado para associações chave → valor.

## Exercício
Crie um contador de frequência de palavras usando `HashMap<String, usize>`.

## Desafio
Ignore diferenças de maiúsculas/minúsculas e apresente as palavras mais frequentes.
