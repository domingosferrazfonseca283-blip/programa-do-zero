# Dia 87 — Observabilidade prática

## Objetivo
Tornar o sistema diagnosticável.

## Conceitos
- logs;
- métricas;
- rastreamento de operações;
- contexto;
- erros;
- correlação entre eventos.

Um log deve ajudar a responder: o que aconteceu, quando, em que operação e com que resultado.

## Prática
Adicione logging estruturado ao projeto:
- início de operação;
- conclusão;
- erro;
- duração quando relevante.

Não registre senhas, tokens ou outros segredos.

## Desafio
Defina indicadores simples para número de operações, erros e latência.

## Checklist
- [ ] Logs têm contexto.
- [ ] Dados sensíveis não aparecem.
- [ ] Erros são diagnosticáveis.
- [ ] Métricas têm significado operacional.
