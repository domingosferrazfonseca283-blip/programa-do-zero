# Dia 95 — CI e qualidade

## Objetivo
Automatizar verificações para impedir regressões.

## Pipeline mínimo
- instalar dependências;
- verificar estilo;
- executar testes;
- produzir resultado;
- falhar quando uma verificação obrigatória falhar.

## Prática
Crie um workflow de CI para o projeto Python.

Inclua testes e uma verificação de qualidade apropriada ao projeto.

## Desafio
Adicione execução em mais de uma versão suportada de Python, quando fizer sentido.

## Checklist
- [ ] CI executa automaticamente.
- [ ] Testes fazem parte do pipeline.
- [ ] Falhas impedem uma execução considerada válida.
- [ ] Dependências são reproduzíveis.
