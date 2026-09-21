# Dia 90 — Injeção de dependências

## Objetivo
Tornar componentes substituíveis e testáveis.

## Teoria
Em vez de uma classe criar diretamente tudo aquilo de que precisa, suas dependências podem ser fornecidas externamente.

Isso facilita:
- testes;
- configuração;
- substituição de infraestrutura;
- reutilização.

## Prática
Faça um serviço receber um repository pelo construtor ou por uma função de fábrica.

Crie:
- repository SQLite;
- repository em memória;
- serviço que funciona com ambos.

## Desafio
Monte a aplicação inteira a partir de uma composição central.

## Checklist
- [ ] Dependências são explícitas.
- [ ] Testes não dependem obrigatoriamente de SQLite.
- [ ] A composição acontece num ponto definido.
