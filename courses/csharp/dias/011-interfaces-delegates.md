# Dia 11 — Interfaces e delegates

## Objetivo
Aprofundar abstração e composição em C#.

## Interfaces
Interfaces definem contratos de comportamento.

```csharp
public interface INotificador
{
    void Enviar(string mensagem);
}
```

## Delegates
Delegates representam referências para métodos compatíveis.

## Exercícios
1. Crie uma interface de persistência.
2. Implemente duas versões.
3. Injete a implementação em um serviço.
4. Crie um delegate para uma regra de validação.

## Desafio
Explique a diferença entre herança de implementação, interface e composição.
