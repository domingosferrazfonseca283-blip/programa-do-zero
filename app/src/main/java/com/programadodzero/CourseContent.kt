package com.programadodzero

/**
 * Modelo de conteúdo pedagógico da aplicação.
 *
 * O conteúdo fica separado das Activities para que possamos adicionar
 * centenas de aulas sem transformar a interface em um arquivo gigante.
 */
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
    private val pythonLessons = listOf(
        LessonContent(
            id = "python-01",
            level = 1, module = 1, order = 1,
            title = "Aula 1 — O que é programação?",
            body = "Programar é dar instruções claras para o computador.",
            code = "print(\"Olá, mundo!\")"
        ),
        LessonContent(
            id = "python-02",
            level = 1, module = 2, order = 1,
            title = "Aula 2 — Variáveis",
            body = "Variáveis guardam informações que podemos usar depois.",
            code = "nome = \"Ana\"\nidade = 20\nprint(nome)"
        ),
        LessonContent(
            id = "python-03",
            level = 1, module = 3, order = 1,
            title = "Aula 3 — Tipos de dados",
            body = "Texto, números, decimais e booleanos representam tipos diferentes.",
            code = "nome = \"Ana\"\nidade = 20\naltura = 1.65\naluno = True"
        ),
        LessonContent(
            id = "python-04",
            level = 1, module = 4, order = 1,
            title = "Aula 4 — Condições",
            body = "Use if para tomar decisões.",
            code = "idade = 20\n\nif idade >= 18:\n    print(\"Maior de idade\")"
        ),
        LessonContent(
            id = "python-05",
            level = 1, module = 5, order = 1,
            title = "Aula 5 — Repetições",
            body = "Use for para repetir tarefas.",
            code = "for numero in range(5):\n    print(numero)"
        ),
        LessonContent(
            id = "python-06",
            level = 2, module = 1, order = 1,
            title = "Aula 6 — Funções",
            body = "Funções agrupam instruções reutilizáveis.",
            code = "def saudacao(nome):\n    return \"Olá, \" + nome"
        ),
        LessonContent(
            id = "python-07",
            level = 2, module = 2, order = 1,
            title = "Aula 7 — Listas",
            body = "Listas guardam vários valores.",
            code = "frutas = [\"maçã\", \"banana\", \"uva\"]"
        ),
        LessonContent(
            id = "python-08",
            level = 2, module = 3, order = 1,
            title = "Aula 8 — Primeiro projeto",
            body = "Vamos juntar os conceitos para criar um pequeno programa.",
            code = "nome = input(\"Seu nome: \")\nprint(\"Olá, \" + nome + \"!\")"
        ),
    private val pythonExercises = listOf(
        ExerciseContent("python-01", "Aula 1 — Mostre uma mensagem", "Escreva um programa que mostre Olá, mundo! usando print().", "print(\"Olá, mundo!\")", "Você acabou de escrever seu primeiro programa."),
        ExerciseContent("python-02", "Aula 2 — Crie uma variável", "Crie uma variável chamada nome e coloque um nome dentro dela.", "nome = \"Ana\"\nprint(nome)", "Variáveis permitem guardar informações."),
        ExerciseContent("python-03", "Aula 3 — Trabalhe com dados", "Crie uma variável de texto e outra com um número.", "nome = \"Ana\"\nidade = 20", "Agora você consegue guardar diferentes tipos de dados."),
        ExerciseContent("python-04", "Aula 4 — Tome uma decisão", "Peça a idade com input(), transforme a resposta em número e use if/else para mostrar mensagens diferentes para menor e maior de idade.", "idade = int(input(\"Digite sua idade: \"))\n\nif idade >= 18:\n    print(\"Maior de idade\")\nelse:\n    print(\"Menor de idade\")", "Agora o programa recebe uma informação e toma uma decisão com base nela."),
        ExerciseContent("python-05", "Aula 5 — Repita uma tarefa", "Use for e range() para mostrar uma sequência de pelo menos três números em ordem crescente.", "for numero in range(5):\n    print(numero)", "Agora você consegue repetir uma tarefa sem copiar o código várias vezes."),
        ExerciseContent("python-06", "Aula 6 — Crie uma função", "Crie uma função que receba um nome, retorne uma saudação usando esse nome e depois mostre o resultado.", "def saudacao(nome):\n    return \"Olá, \" + nome\n\nprint(saudacao(\"Ana\"))", "Agora você criou uma função que recebe dados e devolve um resultado."),
        ExerciseContent("python-07", "Aula 7 — Use uma lista", "Crie uma lista com pelo menos dois itens, mostre a lista e depois mostre o segundo item.", "frutas = [\"maçã\", \"banana\"]\nprint(frutas)\nprint(frutas[1])", "Agora você consegue guardar vários valores e acessar um item específico."),
        ExerciseContent("python-08", "Aula 8 — Primeiro projeto", "Crie um pequeno programa: peça um valor com input(), guarde em uma variável, use if para tomar uma decisão e mostre um resultado com print().", "nome = input(\"Seu nome: \")\n\nif nome:\n    print(\"Olá, \" + nome + \"!\")", "Você juntou entrada, variável, condição e saída em um pequeno projeto.")
    )

    private val additionalPythonExercises = listOf(
        ExerciseContent("python-09", "Aula 9 — Dicionário", "Crie um dicionário com nome e idade e mostre o valor associado à chave nome.", "aluno = {\"nome\": \"Ana\", \"idade\": 20}\nprint(aluno[\"nome\"])", "Você começou a trabalhar com dados estruturados."),
        ExerciseContent("python-10", "Aula 10 — Transforme texto", "Crie uma variável nome e mostre o texto em letras maiúsculas.", "nome = \"Ana\"\nprint(nome.upper())", "Métodos de string permitem transformar texto."),
        ExerciseContent("python-11", "Aula 11 — Proteja a entrada", "Use try/except para tratar uma conversão de número que pode falhar.", "try:\n    numero = int(input(\"Número: \"))\nexcept:\n    print(\"Entrada inválida\")", "Programas robustos tratam situações inesperadas."),
        ExerciseContent("python-12", "Aula 12 — Planeje persistência", "Explique no código, usando comentários, onde você escreveria dados em um arquivo.", "# with open(\"dados.txt\", \"w\") as arquivo:\n#     arquivo.write(\"Olá\")", "Persistência permite manter dados além da execução atual."),
        ExerciseContent("python-13", "Aula 13 — Crie uma classe", "Crie uma classe Pessoa com atributo nome, instancie um objeto e mostre o nome.", "class Pessoa:\n    def __init__(self, nome):\n        self.nome = nome\n\npessoa = Pessoa(\"Ana\")\nprint(pessoa.nome)", "Objetos combinam estado e comportamento."),
        ExerciseContent("python-14", "Aula 14 — Modele uma conta", "Crie uma classe Conta com atributo saldo, instancie-a e mostre o saldo.", "class Conta:\n    def __init__(self, saldo):\n        self.saldo = saldo\n\nconta = Conta(100)\nprint(conta.saldo)", "Agora você está começando a modelar entidades de um sistema.")
    )

    fun exercisesFor(language: String): List<ExerciseContent> =
        if (language == "🐍  Python") pythonExercises + additionalPythonExercises else emptyList()

    fun projectFor(language: String, projectId: Int = 1): ProjectContent? =
        if (language == "🐍  Python" && projectId == 1) pythonProject else null

}
