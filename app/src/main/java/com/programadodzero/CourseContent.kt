package com.programadodzero

data class CourseModule(
    val id: String,
    val title: String,
    val level: Int,
    val order: Int,
    val description: String
)

data class LessonContent(
    val id: String,
    val title: String,
    val body: String,
    val code: String,
    val level: Int = 1,
    val module: Int = 1,
    val order: Int = 0,
    val objectives: List<String> = emptyList(),
    val explanation: String = "",
    val keyPoints: List<String> = emptyList(),
    val reviewQuestion: String = "",
    val reviewAnswer: String = ""
)

data class ExerciseContent(
    val id: String,
    val title: String,
    val instruction: String,
    val starter: String,
    val success: String
)

data class ProjectStepContent(
    val title: String,
    val instruction: String,
    val hint: String,
    val example: String
)

data class ReviewQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val answerIndex: Int,
    val explanation: String,
    val difficulty: Int = 1,
    val topic: String = "Fundamentos"
)

data class ProjectContent(
    val id: Int,
    val title: String,
    val description: String,
    val starter: String,
    val steps: List<ProjectStepContent>
)

object ContentRepository {
    private val pythonModules = listOf(
        CourseModule("python-l1-m1", "Pensamento computacional", 1, 1, "Aprenda a pensar como programador."),
        CourseModule("python-l1-m2", "Variáveis", 1, 2, "Guarde e organize informações."),
        CourseModule("python-l1-m3", "Tipos de dados", 1, 3, "Entenda os principais tipos de valores."),
        CourseModule("python-l1-m4", "Condições", 1, 4, "Faça o programa tomar decisões."),
        CourseModule("python-l1-m5", "Repetições", 1, 5, "Automatize tarefas repetitivas."),
        CourseModule("python-l2-m1", "Funções", 2, 1, "Crie código reutilizável."),
        CourseModule("python-l2-m2", "Listas", 2, 2, "Trabalhe com coleções de dados."),
        CourseModule("python-l2-m3", "Projetos iniciais", 2, 3, "Junte os fundamentos em programas."),
        CourseModule("python-l2-m4", "Dicionários", 2, 4, "Modele dados estruturados."),
        CourseModule("python-l2-m5", "Strings", 2, 5, "Transforme e processe texto."),
        CourseModule("python-l2-m6", "Tratamento de erros", 2, 6, "Crie programas mais resistentes."),
        CourseModule("python-l2-m7", "Arquivos e dados", 2, 7, "Aprenda persistência de dados."),
        CourseModule("python-l3-m1", "Programação orientada a objetos", 3, 1, "Modele entidades com classes e objetos."),
        CourseModule("python-l3-m2", "Organização de sistemas", 3, 2, "Comece a estruturar sistemas maiores."),
        CourseModule("python-l3-m3", "Algoritmos", 3, 3, "Resolva problemas de forma sistemática."),
        CourseModule("python-l3-m4", "Qualidade e testes", 3, 4, "Aprenda práticas para criar código confiável."),
        CourseModule("python-l3-m5", "Arquitetura e projetos", 3, 5, "Estruture soluções maiores e mais profissionais.")
    )

    private val pythonLessons = listOf(
        LessonContent("python-01", "Aula 1 — O que é programação?", "Programar é dar instruções claras para o computador.", "print(\"Olá, mundo!\")", 1, 1, 1, listOf("Identificar o que é programação"), "Entender instruções e sequência", listOf("Um programa é uma sequência de instruções. O computador executa essas instruções seguindo regras precisas."), "Programar é transformar um problema em passos que uma máquina consegue executar.", "O computador não adivinha a intenção: precisamos expressar cada passo com clareza."),
        LessonContent("python-02", "Aula 2 — Variáveis", "Variáveis guardam informações que podemos usar depois.", "nome = \"Ana\"\nidade = 20\nprint(nome)", 1, 2, 1, listOf("Criar e alterar variáveis"), "Usar nomes claros para dados", listOf("Uma variável é um nome associado a um valor. Em Python, a atribuição usa = e o valor pode mudar durante o programa."), "Variáveis permitem guardar dados e reutilizá-los.", "Use nomes que expliquem o significado do dado."),
        LessonContent("python-03", "Aula 3 — Tipos de dados", "Texto, números, decimais e booleanos representam tipos diferentes.", "nome = \"Ana\"\nidade = 20\naltura = 1.65\naluno = True", 1, 3, 1, listOf("Distinguir tipos básicos"), "Escolher o tipo adequado.", listOf("Strings representam texto; int, números inteiros; float, decimais; bool, verdadeiro ou falso."), "Qual a diferença entre \"20\" e 20?", "O primeiro é texto; o segundo é um número inteiro."),
        LessonContent("python-04", "Aula 4 — Condições", "Use if para tomar decisões.", "idade = 20\n\nif idade >= 18:\n    print(\"Maior de idade\")", 1, 4, 1, listOf("Usar if e else"), "Construir decisões com comparações.", listOf("Condições permitem escolher caminhos diferentes."), "Quando o bloco de if é executado?", "Quando a condição é verdadeira."),
        LessonContent("python-05", "Aula 5 — Repetições", "Use for para repetir tarefas.", "for numero in range(5):\n    print(numero)", 1, 5, 1, listOf("Usar for e range"), "Automatizar tarefas repetitivas.", listOf("Laços executam um bloco várias vezes."), "Por que usar um laço?", "Para repetir uma operação sem duplicar código."),
        LessonContent("python-06", "Aula 6 — Funções", "Funções agrupam instruções reutilizáveis.", "def saudacao(nome):\n    return \"Olá, \" + nome", 2, 1, 1, listOf("Definir funções"), "Receber parâmetros e retornar valores", listOf("Funções encapsulam uma tarefa. Parâmetros permitem receber dados e return devolve um resultado para quem chamou."), "Uma boa função tem uma responsabilidade clara e pode ser reutilizada.", "Funções ajudam a reduzir duplicação e organizar sistemas."),
        LessonContent("python-07", "Aula 7 — Listas", "Listas guardam vários valores.", "frutas = [\"maçã\", \"banana\", \"uva\"]", 2, 2, 1, listOf("Criar listas"), "Acessar itens por índice", listOf("Listas armazenam vários valores em uma única estrutura. Em Python, o primeiro índice é 0."), "Podemos consultar, alterar e adicionar elementos conforme o programa evolui.", "O índice 1 representa o segundo elemento."),
        LessonContent("python-08", "Aula 8 — Primeiro projeto", "Vamos juntar os conceitos para criar um pequeno programa.", "nome = input(\"Seu nome: \")\nprint(\"Olá, \" + nome + \"!\")", 2, 3, 1, listOf("Combinar fundamentos"), "Construir um programa pequeno de ponta a ponta", listOf("Projetos são onde conceitos deixam de ser isolados. Entrada, processamento e saída formam um fluxo básico de programa."), "Organize primeiro o problema em passos e depois transforme cada passo em código.", "Um projeto pequeno também deve ser testado com entradas diferentes."),
        LessonContent("python-09", "Aula 9 — Dicionários", "Dicionários associam chaves a valores.", "aluno = {\"nome\": \"Ana\", \"idade\": 20}\nprint(aluno[\"nome\"])", 2, 4, 1, listOf("Criar dicionários"), "Acessar valores por chave.", listOf("Dicionários representam dados por pares chave-valor."), "Como acessar o nome em aluno?", "Usando aluno[\"nome\"]."),
        LessonContent("python-10", "Aula 10 — Strings", "Strings podem ser transformadas e consultadas.", "nome = \"Ana\"\nprint(nome.upper())", 2, 5, 1, listOf("Manipular strings"), "Usar métodos de texto.", listOf("Strings possuem métodos para transformar e analisar texto."), "O que faz upper()?", "Retorna o texto em letras maiúsculas."),
        LessonContent("python-11", "Aula 11 — Tratamento de erros", "Use try/except para tratar entradas inesperadas.", "try:\n    numero = int(input(\"Número: \"))\nexcept:\n    print(\"Entrada inválida\")", 2, 6, 1, listOf("Usar try/except"), "Tratar erros esperados.", listOf("try/except permite reagir quando uma operação falha."), "Para que serve except?", "Para tratar uma exceção ocorrida no try."),
        LessonContent("python-12", "Aula 12 — Arquivos e dados", "Arquivos permitem trabalhar com dados persistentes.", "with open(\"dados.txt\", \"w\") as arquivo:\n    arquivo.write(\"Olá, arquivo!\")", 2, 7, 1, listOf("Ler e escrever arquivos"), "Usar persistência simples.", listOf("with open gerencia o ciclo de vida do arquivo."), "Por que usar with open?", "Para gerenciar o arquivo e garantir seu fechamento."),
        LessonContent("python-15", "Aula 15 — Acumuladores", "Acumuladores permitem construir resultados passo a passo.", "total = 0\nfor numero in range(1, 6):\n    total = total + numero\nprint(total)", 3, 3, 1, listOf("Construir um acumulador"), "Transformar repetição em cálculo progressivo.", listOf("Inicialize o acumulador antes do laço e atualize-o a cada iteração."), "O que é um acumulador?", "Uma variável que guarda o resultado parcial de uma sequência de operações."),
        LessonContent("python-16", "Aula 16 — Busca sequencial", "Uma busca sequencial verifica itens em ordem até encontrar o que procura.", "alvo = 3\nfor numero in range(1, 6):\n    if numero == alvo:\n        print("Encontrado")", 3, 3, 2, listOf("Implementar uma busca simples"), "Percorrer dados e testar uma condição.", listOf("Uma busca pode parar conceitualmente assim que encontra o elemento."), "Qual é a ideia da busca sequencial?", "Examinar os elementos em sequência."),
        LessonContent("python-17", "Aula 17 — Validação de entrada", "Validar dados evita que informações inválidas avancem pelo sistema.", "idade = int(input("Idade: "))\nif idade >= 0:\n    print("Válida")\nelse:\n    print("Inválida")", 3, 4, 1, listOf("Validar dados"), "Separar entrada de regras de validação.", listOf("Validação verifica se uma entrada respeita regras antes de ser usada."), "Por que validar uma entrada?", "Para impedir que dados inválidos causem comportamentos incorretos."),
        LessonContent("python-18", "Aula 18 — Separação de responsabilidades", "Código profissional fica mais fácil de manter quando cada função possui uma responsabilidade clara.", "def dobro(numero):\n    return numero + numero\n\nprint(dobro(5))", 3, 4, 2, listOf("Separar responsabilidades"), "Criar funções pequenas e focadas.", listOf("Funções pequenas facilitam leitura, testes e manutenção."), "Por que dividir um programa em funções?", "Para reduzir complexidade e separar responsabilidades."),
        LessonContent("python-19", "Aula 19 — Pensando em eficiência", "Algoritmos diferentes podem resolver o mesmo problema com custos diferentes.", "for numero in range(5):\n    print(numero)", 3, 5, 1, listOf("Reconhecer custo de algoritmos"), "Comparar quantidade de trabalho.", listOf("Analisar quantas operações uma solução executa ajuda a escolher estratégias melhores."), "O que significa eficiência?", "Resolver o problema usando recursos de forma adequada."),
        LessonContent("python-20", "Aula 20 — Projeto profissional", "Um projeto maior começa com requisitos, entidades, responsabilidades e testes.", "def calcular_total(preco, quantidade):\n    return preco * quantidade\n\nprint(calcular_total(10, 3))", 3, 5, 2, listOf("Estruturar um pequeno projeto"), "Combinar funções, regras e organização.", listOf("Antes de implementar, defina o problema, entradas, saídas e responsabilidades."), "Qual é um bom primeiro passo de um projeto?", "Entender os requisitos e dividir o problema em partes menores."),
        LessonContent("python-13", "Aula 13 — Classes e objetos", "Classes definem estruturas e objetos representam instâncias.", "class Pessoa:\n    def __init__(self, nome):\n        self.nome = nome\n\npessoa = Pessoa(\"Ana\")\nprint(pessoa.nome)", 3, 1, 1, listOf("Criar classes"), "Instanciar objetos", listOf("Uma classe define uma estrutura e seus comportamentos. Um objeto é uma instância concreta dessa classe."), "self representa o próprio objeto dentro de seus métodos.", "Classes ajudam a modelar entidades do domínio."),
        LessonContent("python-14", "Aula 14 — Organização de sistemas", "Comece a modelar entidades de um sistema.", "class Conta:\n    def __init__(self, saldo):\n        self.saldo = saldo\n\nconta = Conta(100)\nprint(conta.saldo)", 3, 2, 1, listOf("Modelar entidades"), "Separar estado e comportamento", listOf("Sistemas maiores ficam mais fáceis de manter quando responsabilidades são distribuídas em estruturas bem definidas."), "Uma classe pode guardar estado e oferecer métodos para alterar esse estado de forma controlada.", "Modele primeiro as entidades e responsabilidades antes de escrever todo o sistema.")
    )

    private val pythonExercises = listOf(
        ExerciseContent("python-01", "Aula 1 — Mostre uma mensagem", "Escreva um programa que mostre Olá, mundo! usando print().", "print(\"Olá, mundo!\")", "Você acabou de escrever seu primeiro programa."),
        ExerciseContent("python-02", "Aula 2 — Crie uma variável", "Crie uma variável chamada nome e coloque um nome dentro dela.", "nome = \"Ana\"\nprint(nome)", "Variáveis permitem guardar informações."),
        ExerciseContent("python-03", "Aula 3 — Trabalhe com dados", "Crie uma variável de texto e outra com um número.", "nome = \"Ana\"\nidade = 20", "Agora você consegue guardar diferentes tipos de dados."),
        ExerciseContent("python-04", "Aula 4 — Tome uma decisão", "Peça a idade com input(), transforme a resposta em número e use if/else para mostrar mensagens diferentes.", "idade = int(input(\"Digite sua idade: \"))\n\nif idade >= 18:\n    print(\"Maior de idade\")\nelse:\n    print(\"Menor de idade\")", "Agora o programa recebe uma informação e toma uma decisão."),
        ExerciseContent("python-05", "Aula 5 — Repita uma tarefa", "Use for e range() para mostrar uma sequência de pelo menos três números.", "for numero in range(5):\n    print(numero)", "Agora você consegue repetir uma tarefa."),
        ExerciseContent("python-06", "Aula 6 — Crie uma função", "Crie uma função que receba um nome, retorne uma saudação e mostre o resultado.", "def saudacao(nome):\n    return \"Olá, \" + nome\n\nprint(saudacao(\"Ana\"))", "Agora você criou uma função."),
        ExerciseContent("python-07", "Aula 7 — Use uma lista", "Crie uma lista com pelo menos dois itens e mostre o segundo item.", "frutas = [\"maçã\", \"banana\"]\nprint(frutas)\nprint(frutas[1])", "Agora você consegue trabalhar com listas."),
        ExerciseContent("python-08", "Aula 8 — Primeiro projeto", "Crie um pequeno programa usando input(), variável, if e print().", "nome = input(\"Seu nome: \")\n\nif nome:\n    print(\"Olá, \" + nome + \"!\")", "Você juntou os fundamentos em um projeto."),
        ExerciseContent("python-09", "Aula 9 — Dicionário", "Crie um dicionário com nome e idade e mostre o valor da chave nome.", "aluno = {\"nome\": \"Ana\", \"idade\": 20}\nprint(aluno[\"nome\"])", "Você começou a trabalhar com dados estruturados."),
        ExerciseContent("python-10", "Aula 10 — Transforme texto", "Crie uma variável nome e mostre o texto em letras maiúsculas.", "nome = \"Ana\"\nprint(nome.upper())", "Métodos de string permitem transformar texto."),
        ExerciseContent("python-11", "Aula 11 — Proteja a entrada", "Use try/except para tratar uma conversão de número que pode falhar.", "try:\n    numero = int(input(\"Número: \"))\nexcept:\n    print(\"Entrada inválida\")", "Programas robustos tratam situações inesperadas."),
        ExerciseContent("python-12", "Aula 12 — Trabalhe com arquivo", "Escreva e leia uma mensagem usando um arquivo virtual.", "with open(\"dados.txt\", \"w\") as arquivo:\n    arquivo.write(\"Olá, arquivo!\")\nwith open(\"dados.txt\", \"r\") as arquivo:\n    print(arquivo.read())", "Agora você consegue trabalhar com persistência."),
        ExerciseContent("python-15", "Aula 15 — Some valores", "Use um acumulador e um laço para calcular a soma de 1 até 5 e mostrar 15.", "total = 0\nfor numero in range(1, 6):\n    total = total + numero\nprint(total)", "Você aprendeu a construir resultados progressivamente."),
        ExerciseContent("python-16", "Aula 16 — Faça uma busca", "Percorra 1 até 5 e mostre Encontrado quando encontrar o número 3.", "alvo = 3\nfor numero in range(1, 6):\n    if numero == alvo:\n        print("Encontrado")", "Você implementou uma busca sequencial."),
        ExerciseContent("python-17", "Aula 17 — Valide uma idade", "Receba uma idade e mostre Válida para valores maiores ou iguais a zero.", "idade = int(input("Idade: "))\nif idade >= 0:\n    print("Válida")\nelse:\n    print("Inválida")", "Você começou a validar dados."),
        ExerciseContent("python-18", "Aula 18 — Separe uma responsabilidade", "Crie uma função dobro(numero) que retorne o dobro e mostre o resultado.", "def dobro(numero):\n    return numero + numero\n\nprint(dobro(5))", "Funções pequenas tornam o código mais organizado."),
        ExerciseContent("python-19", "Aula 19 — Percorra dados", "Use um laço para mostrar cinco números em sequência.", "for numero in range(5):\n    print(numero)", "Você praticou uma solução simples e previsível."),
        ExerciseContent("python-20", "Aula 20 — Organize uma regra", "Crie calcular_total(preco, quantidade), retorne o total e mostre o resultado.", "def calcular_total(preco, quantidade):\n    return preco * quantidade\n\nprint(calcular_total(10, 3))", "Você estruturou uma pequena regra de negócio."),
        ExerciseContent("python-13", "Aula 13 — Crie uma classe", "Crie uma classe Pessoa com atributo nome, instancie um objeto e mostre o nome.", "class Pessoa:\n    def __init__(self, nome):\n        self.nome = nome\n\npessoa = Pessoa(\"Ana\")\nprint(pessoa.nome)", "Objetos combinam estado e comportamento."),
        ExerciseContent("python-14", "Aula 14 — Modele uma conta", "Crie uma classe Conta com saldo e um método depositar que leve o saldo até 150.", "class Conta:\n    def __init__(self, saldo):\n        self.saldo = saldo\n\n    def depositar(self, valor):\n        self.saldo = self.saldo + valor\n\nconta = Conta(100)\nconta.depositar(50)\nprint(conta.saldo)", "Agora você está modelando uma entidade de sistema.")
    )

    private val pythonReviewQuestions = mapOf(
        "python-01" to ReviewQuestion("python-01-q1", "O que é programação?", listOf("Criar instruções para um computador", "Montar um computador", "Somente escrever textos", "Usar internet"), 0, "Programação consiste em criar instruções executáveis para resolver tarefas.", 1, "Pensamento computacional"),
        "python-02" to ReviewQuestion("python-02-q1", "Para que serve uma variável?", listOf("Guardar um valor", "Desligar o computador", "Criar uma senha", "Abrir um arquivo"), 0, "Uma variável associa um nome a um valor.", 1, "Variáveis"),
        "python-03" to ReviewQuestion("python-03-q1", "Qual é um valor booleano?", listOf("True", "20", "Ana", "1.5"), 0, "Booleanos representam verdadeiro ou falso.", 1, "Tipos de dados"),
        "python-04" to ReviewQuestion("python-04-q1", "Quando o bloco if é executado?", listOf("Quando a condição é verdadeira", "Sempre", "Nunca", "Somente depois de um for"), 0, "O if escolhe um caminho quando sua condição é verdadeira.", 1, "Condições"),
        "python-05" to ReviewQuestion("python-05-q1", "Para que serve um laço?", listOf("Repetir uma tarefa", "Criar uma variável", "Apagar código", "Fechar o aplicativo"), 0, "Laços automatizam repetições.", 1, "Repetições"),
        "python-06" to ReviewQuestion("python-06-q1", "O que um parâmetro fornece?", listOf("Dados para uma função", "Energia ao computador", "Um arquivo", "Uma senha"), 0, "Parâmetros permitem que funções recebam dados.", 2, "Funções"),
        "python-07" to ReviewQuestion("python-07-q1", "Qual é o primeiro índice de uma lista Python?", listOf("0", "1", "-1", "10"), 0, "Python usa indexação baseada em zero.", 2, "Listas"),
        "python-08" to ReviewQuestion("python-08-q1", "Quais são partes comuns de um programa?", listOf("Entrada, processamento e saída", "Tela, teclado e mouse", "Arquivo, pasta e rede", "Classe, objeto e servidor"), 0, "Esse fluxo representa uma estrutura básica de processamento.", 2, "Projetos"),
        "python-09" to ReviewQuestion("python-09-q1", "Como acessar aluno['nome']?", listOf("Pela chave nome", "Pelo índice 0", "Com input()", "Com range()"), 0, "Dicionários são acessados por chaves.", 2, "Dicionários"),
        "python-10" to ReviewQuestion("python-10-q1", "O que upper() faz?", listOf("Converte para maiúsculas", "Apaga o texto", "Converte para números", "Cria uma lista"), 0, "upper() retorna uma versão em letras maiúsculas.", 2, "Strings"),
        "python-11" to ReviewQuestion("python-11-q1", "Para que serve except?", listOf("Tratar uma exceção", "Criar uma classe", "Repetir um laço", "Ler uma lista"), 0, "except define o tratamento quando uma exceção ocorre.", 2, "Erros"),
        "python-12" to ReviewQuestion("python-12-q1", "Qual modo abre arquivo para leitura?", listOf("r", "w", "a", "x"), 0, "O modo r é usado para leitura.", 2, "Arquivos"),
        "python-13" to ReviewQuestion("python-13-q1", "O que é um objeto?", listOf("Uma instância de uma classe", "Sempre um arquivo", "Uma função global", "Um comentário"), 0, "Um objeto é uma instância concreta de uma classe.", 3, "Orientação a objetos"),
        "python-14" to ReviewQuestion("python-14-q1", "O que a orientação a objetos organiza?", listOf("Estado e comportamento", "Somente textos", "Somente arquivos", "A conexão Wi-Fi"), 0, "Classes ajudam a organizar estado e comportamento das entidades.", 3, "Organização de sistemas"),
        "python-15" to ReviewQuestion("python-15-q1", "O que é um acumulador?", listOf("Uma variável que guarda um resultado parcial", "Um arquivo", "Um tipo de erro", "Uma classe"), 0, "O acumulador é atualizado durante uma sequência de operações.", 3, "Algoritmos"),
        "python-16" to ReviewQuestion("python-16-q1", "Como funciona uma busca sequencial?", listOf("Verifica os itens em ordem", "Escolhe sempre o último item", "Ordena tudo automaticamente", "Apaga itens"), 0, "A busca sequencial examina os elementos um por um.", 3, "Algoritmos"),
        "python-17" to ReviewQuestion("python-17-q1", "Por que validar dados?", listOf("Para rejeitar entradas inválidas", "Para aumentar a tela", "Para apagar variáveis", "Para criar classes"), 0, "Validação protege as regras do programa.", 3, "Qualidade e testes"),
        "python-18" to ReviewQuestion("python-18-q1", "Qual é uma vantagem de funções pequenas?", listOf("Separar responsabilidades", "Eliminar todos os testes", "Evitar variáveis", "Impedir reutilização"), 0, "Responsabilidades menores facilitam manutenção e testes.", 3, "Arquitetura"),
        "python-19" to ReviewQuestion("python-19-q1", "O que análise de eficiência ajuda a avaliar?", listOf("O trabalho e recursos usados", "A cor do aplicativo", "O nome do computador", "A velocidade da internet"), 0, "Eficiência considera recursos necessários para executar uma solução.", 3, "Eficiência"),
        "python-20" to ReviewQuestion("python-20-q1", "O que fazer antes de implementar um projeto?", listOf("Entender requisitos e dividir o problema", "Escrever tudo de uma vez", "Ignorar entradas", "Evitar testes"), 0, "Entender o problema reduz retrabalho e orienta a implementação.", 3, "Projetos profissionais")
    )

    fun questionBank(language: String): List<ReviewQuestion> = if (language == "🐍  Python") pythonReviewQuestions.values.toList() else emptyList()

    fun finalExamFor(language: String): List<ReviewQuestion> { if (language != "🐍  Python") return emptyList(); val all = pythonReviewQuestions.values.toList(); val seed = java.time.LocalDate.now().toEpochDay().toInt(); return all.sortedBy { it.id.hashCode() xor seed }.take(10) }

    fun reviewFor(language: String, lessonId: String): ReviewQuestion? =
        if (language == "🐍  Python") pythonReviewQuestions[lessonId] else null

    private val pythonProject = ProjectContent(
        1, "Jogo de Adivinhação",
        "Construa um jogo simples usando variável, input, condições e repetição.",
        "numero_secreto = 7\ntentativas = 0",
        listOf(
            ProjectStepContent("1. Defina o segredo", "Crie a variável numero_secreto.", "Use numero_secreto = 7.", "numero_secreto = 7"),
            ProjectStepContent("2. Receba o palpite", "Peça um palpite ao jogador com input().", "Guarde a resposta em uma variável.", "palpite = int(input(\"Palpite: \"))"),
            ProjectStepContent("3. Compare", "Compare o palpite com o número secreto usando if.", "Use == para verificar igualdade.", "if palpite == numero_secreto:"),
            ProjectStepContent("4. Dê feedback", "Mostre uma mensagem quando o jogador acertar ou errar.", "Use else e print().", "else:\n    print(\"Tente novamente\")"),
            ProjectStepContent("5. Conte tentativas", "Use while e aumente tentativas a cada rodada.", "Some 1 à variável tentativas.", "tentativas = tentativas + 1")
        )
    )

    fun modulesFor(language: String): List<CourseModule> =
        if (language == "🐍  Python") pythonModules else emptyList()

    fun modulesForLevel(language: String, level: Int): List<CourseModule> =
        modulesFor(language).filter { it.level == level }.sortedBy { it.order }

    fun lessonsFor(language: String): List<LessonContent> =
        if (language == "🐍  Python") pythonLessons.sortedWith(compareBy({ it.level }, { it.module }, { it.order })) else emptyList()

    fun lessonsForLevel(language: String, level: Int): List<LessonContent> =
        lessonsFor(language).filter { it.level == level }

    fun lessonsForModule(language: String, level: Int, module: Int): List<LessonContent> =
        lessonsFor(language).filter { it.level == level && it.module == module }.sortedBy { it.order }

    fun exerciseFor(language: String, lessonId: String): ExerciseContent? =
        exercisesFor(language).firstOrNull { it.id == lessonId }

    fun exercisesFor(language: String): List<ExerciseContent> =
        if (language == "🐍  Python") pythonExercises else emptyList()

    fun projectFor(language: String, projectId: Int = 1): ProjectContent? =
        if (language == "🐍  Python" && projectId == 1) pythonProject else null

    fun isLanguageAvailable(language: String): Boolean =
        language == "🐍  Python"

    fun availableLanguages(): List<String> =
        listOf("🐍  Python")
}
