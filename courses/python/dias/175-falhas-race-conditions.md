# Dia 175 — Falhas e race conditions

## Objetivo
Aprender a encontrar e corrigir condições de corrida em programas concorrentes.

## Laboratório
Crie um contador compartilhado e faça vários workers incrementarem seu valor. Primeiro implemente uma versão propositalmente insegura. Depois reproduza a falha com sincronização controlada, explique o interleaving, corrija com lock e crie uma alternativa baseada em mensagens.

## Fault injection
Introduza atrasos artificiais em pontos específicos para ampliar a janela de corrida. O objetivo é diagnóstico em ambiente local.

## Desafio
Escolha um recurso com múltiplos estados e encontre uma atualização que precise ser atômica.

## Checklist
- [ ] Sei explicar uma race condition.
- [ ] Consigo reproduzir uma falha controladamente.
- [ ] Sei proteger uma seção crítica.
- [ ] Sei avaliar uma alternativa sem estado compartilhado.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
