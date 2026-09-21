# Dia 14 — Organização de projeto Go

## Objetivo
Transformar código funcional em projeto sustentável.

## Estrutura sugerida
```
cmd/
internal/
pkg/
configs/
tests/
```

A estrutura deve servir ao projeto; não existe obrigação de criar diretórios apenas por convenção.

## Exercício
Separe um pequeno programa em:
- domínio;
- serviço;
- persistência;
- entrada da aplicação.

## Desafio
Explique por escrito por que cada pacote existe e qual responsabilidade ele possui.

## Regra
Evite dependências circulares e pacotes com responsabilidades indefinidas.
