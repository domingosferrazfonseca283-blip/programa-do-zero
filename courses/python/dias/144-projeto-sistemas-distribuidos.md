# Dia 144 — Projeto: processo distribuído

## Objetivo
Integrar domínio, persistência, eventos e processamento assíncrono.

## Cenário
Construa, em laboratório, um fluxo de pedidos com:
- domínio;
- repository;
- Unit of Work;
- outbox;
- consumidor;
- idempotência;
- retries;
- estados;
- auditoria;
- métricas;
- testes.

## Entrega
Documente o fluxo completo e pelo menos cinco cenários de falha.

## Critério
O projeto deve explicar claramente o que acontece quando:
- o banco falha;
- o consumidor falha;
- a mensagem é duplicada;
- uma etapa demora;
- uma etapa não pode ser concluída.

## Checklist
- [ ] Fluxo normal funciona.
- [ ] Falhas são previsíveis.
- [ ] Duplicação é tratada.
- [ ] Estado pode ser auditado.
