# Dia 17 — Generics

## Objetivo
Criar código reutilizável sem perder segurança de tipos.

## Exemplo
```rust
fn maior<T: Ord>(a: T, b: T) -> T {
    if a > b { a } else { b }
}
```

## Exercício
Crie funções genéricas sobre coleções.

## Desafio
Combine generics com traits para restringir corretamente o comportamento aceito.
