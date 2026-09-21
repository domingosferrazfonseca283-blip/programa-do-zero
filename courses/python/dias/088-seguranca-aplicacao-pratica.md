# Dia 88 — Segurança da aplicação

## Objetivo
Aplicar segurança durante o desenvolvimento, não apenas no final.

## Princípios
- validar entradas;
- autenticar quando necessário;
- autorizar ações;
- usar queries parametrizadas;
- proteger segredos;
- limitar privilégios;
- atualizar dependências;
- evitar exposição de dados sensíveis.

## Prática
Faça uma revisão de segurança do projeto:
1. entradas;
2. autenticação;
3. autorização;
4. banco;
5. ficheiros;
6. configuração;
7. logs;
8. dependências.

## Desafio
Crie uma lista de ameaças e respectivas mitigações.

## Checklist
- [ ] Validação existe.
- [ ] Autorização é explícita.
- [ ] SQL é parametrizado.
- [ ] Segredos ficam fora do código.
- [ ] Logs não expõem credenciais.
