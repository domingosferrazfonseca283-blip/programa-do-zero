# Dia 13 — Traits

## Objetivo
Aprender abstrações centrais de Rust.

## Conceito
Traits descrevem comportamento compartilhado.

```rust
trait Descrever {
    fn descricao(&self) -> String;
}
```

## Exercício
Crie uma trait para entidades que possam ser exibidas.

## Desafio
Implemente a mesma trait para dois tipos diferentes e escreva uma função genérica que trabalhe com ambos.

## Pergunta
Que comportamento pertence à abstração e que comportamento pertence ao tipo concreto?
