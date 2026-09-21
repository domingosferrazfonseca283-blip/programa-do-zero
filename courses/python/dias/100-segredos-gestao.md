# Dia 100 — Gestão de segredos

## Objetivo
Tratar credenciais e segredos corretamente.

## Nunca faça
- colocar tokens no Git;
- imprimir credenciais em logs;
- enviar segredos para respostas HTTP;
- reutilizar credenciais reais em testes.

## Prática
Crie uma configuração que leia um segredo do ambiente e valide sua presença sem revelar seu valor.

## Desafio
Procure no projeto possíveis vazamentos de segredos e documente a correção.

## Checklist
- [ ] Segredos ficam fora do código.
- [ ] Logs não revelam segredos.
- [ ] Testes usam valores fictícios.
