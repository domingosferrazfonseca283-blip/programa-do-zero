# Dia 010 — Exceções e Tratamento de Erros

## Objetivo
Aprender a lidar com situações que impedem uma operação de concluir normalmente.

```python
try:
    idade = int(input("Idade: "))
except ValueError:
    print("Digite um número válido.")
```

## Exercícios
1. Tratar conversão inválida.
2. Tratar divisão por zero.
3. Validar entrada até ficar correta.
4. Separar tratamento de erro da regra principal.
5. Criar mensagens úteis para o usuário.

## Desafio
Transformar a calculadora anterior em uma aplicação que não encerra inesperadamente quando recebe entradas inválidas.
