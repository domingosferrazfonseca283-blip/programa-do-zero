# Python — Volume 1: Fundamentos

Cada aula segue: objetivo, explicação simples, demonstração, prática guiada, exercícios, desafio e revisão.

## Aula 1 — Primeiro programa
```python
print("Olá, mundo!")
print("Estou a aprender Python.")
```
Exercícios: mostrar nome; três objetivos; pequena apresentação.

## Aula 2 — Variáveis e tipos
```python
nome="Ana"
idade=20
altura=1.70
estudante=True
```
Exercício: criar variáveis para nome, idade, cidade, saldo e estado.

## Aula 3 — Operadores
```python
a=10
b=3
print(a+b,a-b,a*b)
print(a/b,a//b,a%b,a**b)
```
Desafio: calculadora e conversor de temperatura.

## Aula 4 — Entrada
```python
nome=input("Nome: ")
idade=int(input("Idade: "))
print(f"{nome} tem {idade} anos.")
```

## Aula 5 — Condições
```python
idade=int(input("Idade: "))
if idade>=18:
    print("Maior de idade")
else:
    print("Menor de idade")
```

## Aula 6 — Ciclos
```python
for n in range(1,6):
    print(n)
```
Desafio: tabela de multiplicação.

## Aula 7 — Listas
```python
frutas=["maçã","banana","laranja"]
frutas.append("pera")
```

## Aula 8 — Dicionários
```python
utilizador={"nome":"Ana","idade":30}
print(utilizador["nome"])
```

## Aula 9 — Funções
```python
def dobro(x):
    return x*2
```

## Aula 10 — Exceções
```python
try:
    idade=int(input("Idade: "))
except ValueError:
    print("Entrada inválida")
```

## Aula 11 — Ficheiros
```python
from pathlib import Path
Path("notas.txt").write_text("Estudar Python\n",encoding="utf-8")
```

## Aula 12 — Projeto
Gestor de notas com menu, funções, validação e JSON.

### Rotina diária
30 minutos de teoria, 45 de código, 30 de exercícios e 15 de revisão.