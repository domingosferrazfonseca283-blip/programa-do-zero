# Dia 84 — Service Layer

## Objetivo
Orquestrar regras de negócio sem transformar a interface num centro de lógica.

## Teoria
Uma camada de serviço coordena operações como:
1. receber dados;
2. validar regras;
3. consultar persistência;
4. executar a operação;
5. devolver resultado.

A camada de serviço não deve depender dos detalhes da CLI ou da API.

## Prática
Implemente serviços para:
- criar tarefa;
- concluir tarefa;
- procurar tarefas;
- remover tarefa.

## Desafio
Faça a mesma lógica ser usada por uma CLI e por uma API sem duplicação.

## Checklist
- [ ] Serviços têm responsabilidades claras.
- [ ] Interface não contém regras centrais.
- [ ] Persistência está isolada.
- [ ] Testei fluxos de sucesso e erro.
