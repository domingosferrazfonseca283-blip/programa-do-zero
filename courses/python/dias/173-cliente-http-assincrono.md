# Dia 173 — Cliente HTTP assíncrono

## Objetivo
Construir um cliente HTTP assíncrono previsível contra um serviço local de laboratório.

## Conceitos
- timeout;
- cancelamento;
- retry com limite;
- backoff;
- idempotência;
- tratamento de status;
- limite de concorrência.

## Prática guiada
Crie um servidor HTTP local com endpoints que respondem normalmente, atrasam, retornam erro temporário e retornam erro permanente. Implemente o cliente com timeout e retry apenas para operações seguras.

## Regra importante
Retry não é sinônimo de resiliência. Repetir uma operação não idempotente pode duplicar efeitos.

## Desafio
Adicione jitter ao backoff, limite total de tentativas e métricas de sucesso/falha.

## Checklist
- [ ] Uso timeout explícito.
- [ ] Distingo erros temporários de permanentes.
- [ ] Tenho limite de retries.
- [ ] Considerei idempotência.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
