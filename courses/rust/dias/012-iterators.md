# Dia 12 — Iterators

## Objetivo
Entender o modelo de iteração de Rust.

## Conceito
Iterators permitem construir transformações de dados de maneira composicional.

```rust
let resultado: Vec<i32> = numeros
    .iter()
    .filter(|n| **n > 0)
    .map(|n| n * 2)
    .collect();
```

## Exercício
Faça filtros, transformações e agregações sobre um vetor.

## Desafio
Resolva o mesmo problema com um loop tradicional e com iterators. Compare legibilidade.
