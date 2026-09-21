# Dia 110 — Retries e backoff

## Objetivo
Implementar novas tentativas sem transformar uma falha temporária em sobrecarga.

## Teoria
Retries devem ser limitados e usados principalmente quando a falha pode ser temporária.

Backoff aumenta o intervalo entre tentativas.

## Prática
Implemente uma política com:
- número máximo de tentativas;
- intervalo;
- erro considerado recuperável;
- erro definitivo.

## Desafio
Adicione jitter para evitar que muitos clientes repitam ao mesmo tempo.

## Checklist
- [ ] Há limite de tentativas.
- [ ] Diferenciei falhas recuperáveis.
- [ ] Evitei retry infinito.
