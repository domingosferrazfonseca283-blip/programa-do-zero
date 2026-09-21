# Dia 14 — Módulos e tratamento de erros

## Objetivo
Organizar código e modelar falhas.

## Conceitos
Use módulos para separar responsabilidades e `Result<T, E>` para operações que podem falhar.

```rust
fn ler_dado() -> Result<String, std::io::Error> {
    // ...
}
```

## Exercício
Crie uma função que leia um arquivo e retorne um erro apropriado.

## Desafio
Propague erros sem usar `unwrap()` em caminhos normais da aplicação.
