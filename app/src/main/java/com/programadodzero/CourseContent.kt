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
    val order: Int = 0
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
        CourseModule("python-l3-m2", "Organização de sistemas", 3, 2, "Comece a estruturar sistemas maiores.")
    )

    private val pythonLessons = listOf(
        LessonContent("python-01", "Aula 1 — O que é programação?", "Programar é dar instruções claras para o computador.", "print(\"Olá, mundo!\")", 1, 1, 1),
        LessonContent("python-02", "Aula 2 — Variáveis", "Variáveis guardam informações que podemos usar depois.", "nome = \"Ana\"\nidade = 20\nprint(nome)", 1, 2, 1),
        LessonContent("python-03", "Aula 3 — Tipos de dados", "Texto, números, decimais e booleanos representam tipos diferentes.", "nome = \"Ana\"\nidade = 20\naltura = 1.65\naluno = True", 1, 3, 1),
        LessonContent("python-04", "Aula 4 — Condições", "Use if para tomar decisões.", "idade = 20\n\nif idade >= 18:\n    print(\"Maior de idade\")", 1, 4, 1),
        LessonContent("python-05", "Aula 5 — Repetições", "Use for para repetir tarefas.", "for numero in range(5):\n    print(numero)", 1, 5, 1),
        LessonContent("python-06", "Aula 6 — Funções", "Funções agrupam instruções reutilizáveis.", "def saudacao(nome):\n    return \"Olá, \" + nome", 2, 1, 1),
        LessonContent("python-07", "Aula 7 — Listas", "Listas guardam vários valores.", "frutas = [\"maçã\", \"banana\", \"uva\"]", 2, 2, 1),
        LessonContent("python-08", "Aula 8 — Primeiro projeto", "Vamos juntar os conceitos para criar um pequeno programa.", "nome = input(\"Seu nome: \")\nprint(\"Olá, \" + nome + \"!\")", 2, 3, 1),
        LessonContent("python-09", "Aula 9 — Dicionários", "Dicionários associam chaves a valores.", "aluno = {\"nome\": \"Ana\", \"idade\": 20}\nprint(aluno[\"nome\"])", 2, 4, 1),
        LessonContent("python-10", "Aula 10 — Strings", "Strings podem ser transformadas e consultadas.", "nome = \"Ana\"\nprint(nome.upper())", 2, 5, 1),
        LessonContent("python-11", "Aula 11 — Tratamento de erros", "Use try/except para tratar entradas inesperadas.", "try:\n    numero = int(input(\"Número: \"))\nexcept:\n    print(\"Entrada inválida\")", 2, 6, 1),
        LessonContent("python-12", "Aula 12 — Arquivos e dados", "Arquivos permitem trabalhar com dados persistentes.", "with open(\"dados.txt\", \"w\") as arquivo:\n    arquivo.write(\"Olá, arquivo!\")", 2, 7, 1),
        LessonContent("python-13", "Aula 13 — Classes e objetos", "Classes definem estruturas e objetos representam instâncias.", "class Pessoa:\n    def __init__(self, nome):\n        self.nome = nome\n\npessoa = Pessoa(\"Ana\")\nprint(pessoa.nome)", 3, 1, 1),
        LessonContent("python-14", "Aula 14 — Organização de sistemas", "Comece a modelar entidades de um sistema.", "class Conta:\n    def __init__(self, saldo):\n        self.saldo = saldo\n\nconta = Conta(100)\nprint(conta.saldo)", 3, 2, 1)
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
        ExerciseContent("python-13", "Aula 13 — Crie uma classe", "Crie uma classe Pessoa com atributo nome, instancie um objeto e mostre o nome.", "class Pessoa:\n    def __init__(self, nome):\n        self.nome = nome\n\npessoa = Pessoa(\"Ana\")\nprint(pessoa.nome)", "Objetos combinam estado e comportamento."),
        ExerciseContent("python-14", "Aula 14 — Modele uma conta", "Crie uma classe Conta com saldo e um método depositar que leve o saldo até 150.", "class Conta:\n    def __init__(self, saldo):\n        self.saldo = saldo\n\n    def depositar(self, valor):\n        self.saldo = self.saldo + valor\n\nconta = Conta(100)\nconta.depositar(50)\nprint(conta.saldo)", "Agora você está modelando uma entidade de sistema.")
    )

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
