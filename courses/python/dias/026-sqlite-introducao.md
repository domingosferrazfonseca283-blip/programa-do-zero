# Dia 026 — SQLite

## Conteúdo
Banco relacional local, tabelas, registros, conexão, consultas e transações.

```python
import sqlite3
con = sqlite3.connect("app.db")
```

## Segurança
Use consultas parametrizadas; nunca monte SQL concatenando entrada do utilizador.

## Exercícios
Criar tabela de tarefas, inserir, consultar, atualizar e remover registros.
