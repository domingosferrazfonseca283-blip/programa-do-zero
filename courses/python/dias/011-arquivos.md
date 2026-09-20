# Dia 011 — Arquivos

## Conteúdo
Leitura, escrita, `with`, caminhos e dados estruturados.

```python
with open("notas.txt", "r", encoding="utf-8") as arquivo:
    conteudo = arquivo.read()
```

## Exercícios
1. Criar arquivo de notas.
2. Ler linhas e contar registros.
3. Gravar resultados.
4. Trabalhar com JSON.
5. Tratar arquivo inexistente.

## Desafio
Persistir o gerenciador de tarefas em um arquivo JSON, mantendo validação e tratamento de erros.

## Segurança
Use somente diretórios de laboratório. Evite sobrescrever arquivos importantes sem confirmação ou modo de simulação.
