# Dia 13 — LINQ

## Objetivo
Aprender consultas expressivas sobre coleções.

## Exemplo

```csharp
var ativos = usuarios
    .Where(u => u.Ativo)
    .OrderBy(u => u.Nome)
    .Select(u => u.Nome);
```

## Exercícios
Pratique Where, Select, OrderBy, GroupBy, Any, All, FirstOrDefault e ToDictionary.

## Desafio
Pegue uma coleção de vendas e produza um relatório agrupado por categoria.

## Atenção
Entenda quando uma consulta é executada e evite materializações desnecessárias.
