# Dia 115 — Timeouts

## Objetivo
Impedir que uma dependência lenta mantenha recursos ocupados indefinidamente.

## Prática
Defina timeouts separados para:
- conexão;
- leitura;
- operação completa.

Simule uma dependência lenta e verifique o comportamento.

## Desafio
Explique por que timeout não é simplesmente um valor arbitrário.

## Checklist
- [ ] Timeouts existem.
- [ ] Falhas de timeout são tratadas.
- [ ] O usuário recebe uma resposta controlada.
