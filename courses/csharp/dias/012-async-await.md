# Dia 12 — async e await

## Objetivo
Compreender programação assíncrona no .NET.

```csharp
public async Task<string> LerAsync()
{
    return await File.ReadAllTextAsync("dados.txt");
}
```

## Conceitos
- Task;
- await;
- cancelamento;
- exceções assíncronas;
- operações de I/O.

## Exercício
Crie uma operação assíncrona que leia vários arquivos.

## Desafio
Adicione CancellationToken e evite bloquear a thread desnecessariamente.
