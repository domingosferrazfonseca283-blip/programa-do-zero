# Dia 92 — API e idempotência

## Objetivo
Entender como operações repetidas podem afetar sistemas.

## Teoria
Uma operação idempotente pode ser repetida sem produzir efeitos adicionais indesejados. Isso é importante em sistemas distribuídos, retries e integrações.

Analise:
- GET;
- PUT;
- DELETE;
- POST.

## Prática
Projete uma operação de criação que possa lidar com repetição usando uma chave de idempotência.

## Desafio
Simule uma requisição repetida e prove por teste que o sistema não cria duplicações indevidas.

## Checklist
- [ ] Entendi idempotência.
- [ ] Identifiquei operações sensíveis a retry.
- [ ] Criei um teste de repetição.
