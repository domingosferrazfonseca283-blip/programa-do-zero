# Dia 91 — API: erros e contratos

## Objetivo
Projetar respostas de API previsíveis.

## Teoria
Uma API deve possuir contratos claros para:
- entradas válidas;
- respostas;
- erros;
- códigos HTTP;
- validação.

Erros internos não devem revelar detalhes sensíveis ao cliente.

## Prática
Defina respostas para:
- recurso criado;
- recurso inexistente;
- entrada inválida;
- operação não autorizada;
- erro interno.

## Desafio
Crie uma estrutura consistente de erro e testes para cada caso.

## Checklist
- [ ] Contratos são claros.
- [ ] Códigos HTTP são coerentes.
- [ ] Erros não expõem detalhes internos.
- [ ] Casos de erro têm testes.
