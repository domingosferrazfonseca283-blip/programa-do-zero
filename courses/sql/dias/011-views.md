# Dia 11 — Views

## Objetivo
Criar consultas reutilizáveis através de views.

## Exemplo
```sql
CREATE VIEW vendas_por_cliente AS
SELECT cliente_id, SUM(valor) AS total
FROM vendas
GROUP BY cliente_id;
```

## Exercício
Crie views para relatórios frequentes.

## Desafio
Explique quais consultas devem permanecer dinâmicas e quais se beneficiam de uma view.
