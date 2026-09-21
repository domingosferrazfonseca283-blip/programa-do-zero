# Dia 169 — Coordenação distribuída

## Objetivo
Entender como coordenar trabalho entre processos/serviços sem depender de estado compartilhado inseguro.

## Conceitos
- lock lógico e exclusão mútua;
- lease com expiração;
- eleição de líder como conceito arquitetural;
- idempotência;
- falhas parciais e perda de comunicação.

## Prática guiada
Implemente um simulador local de tarefas com identificador único. Antes de executar uma tarefa, o worker consulta uma estrutura em memória que representa um lock. Simule atrasos e faça o worker liberar o recurso mesmo quando a tarefa falhar.

## Desafio
Modele dois workers competindo pelo mesmo recurso e prove, com testes, que apenas um executa a seção crítica por vez.

## Checklist
- [ ] Entendo lock versus lease.
- [ ] Sei por que idempotência reduz efeitos de retries.
- [ ] Consigo modelar falhas de comunicação.
- [ ] Testei concorrência em ambiente controlado.

## Exercício diário
Reserve 60–90 minutos: 15 min de leitura, 25 min de implementação, 20 min de testes e 10 min de revisão escrita.
