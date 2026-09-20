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
        )
    )

    private val genericLessons = listOf(
        LessonContent(
            id = "generic-01",
            title = "Aula 1 — O que é programação?",
            body = "Programar é escrever instruções para o computador.",
            code = "print(\"Olá, mundo!\")"
        ),
        LessonContent(
            id = "generic-02",
            title = "Aula 2 — Variáveis",
            body = "Uma variável guarda um valor.",
            code = "nome = \"Ana\""
        ),
        LessonContent(
            id = "generic-03",
            title = "Aula 3 — Dados e tipos",
            body = "Programas trabalham com diferentes tipos de dados.",
            code = "texto = \"Olá\"\nnumero = 10"
        ),
        LessonContent(
            id = "generic-04",
            title = "Aula 4 — Decisões",
            body = "Condições permitem escolher o que fazer.",
            code = "if idade >= 18 { }"
        ),
        LessonContent(
            id = "generic-05",
            title = "Aula 5 — Repetições",
            body = "Laços repetem tarefas.",
            code = "for (item in itens) { }"
        )
    )

    private val pythonModules = listOf(
        CourseModule("python-l1-m1", "Pensamento computacional", 1, 1, "Aprenda a pensar em passos e transformar problemas em instruções."),
        CourseModule("python-l1-m2", "Variáveis", 1, 2, "Aprenda a guardar e reutilizar informações."),
        CourseModule("python-l1-m3", "Tipos de dados", 1, 3, "Conheça texto, números e valores booleanos."),
        CourseModule("python-l1-m4", "Condições", 1, 4, "Faça o programa tomar decisões."),
        CourseModule("python-l1-m5", "Repetições", 1, 5, "Automatize tarefas repetitivas."),
        CourseModule("python-l2-m1", "Funções", 2, 1, "Crie blocos reutilizáveis e organize seus programas."),
        CourseModule("python-l2-m2", "Listas", 2, 2, "Trabalhe com coleções de valores."),
        CourseModule("python-l2-m3", "Projetos iniciais", 2, 3, "Combine conceitos para construir programas."),
        CourseModule("python-l3-m1", "Programação orientada a objetos", 3, 1, "Prepare-se para modelar sistemas maiores.")
    )

    fun modulesFor(language: String): List<CourseModule> =
        if (language == "🐍  Python") pythonModules else emptyList()

    fun lessonsForLevel(language: String, level: Int): List<LessonContent> =
        lessonsFor(language).filter { it.level == level }.sortedWith(compareBy({ it.module }, { it.order }))

    fun lessonsFor(language: String): List<LessonContent> =
        if (language == "🐍  Python") pythonLessons else emptyList()

    fun isLanguageAvailable(language: String): Boolean = language == "🐍  Python"

    fun availableLanguages(): List<String> = listOf("🐍  Python")


    private val pythonProject = ProjectContent(
        id = 1,
        title = "Projeto 1 — Jogo de Adivinhação",
        description = "Construa um jogo em Python por etapas, depois execute o programa completo.",
        starter = "numero_secreto = 7",
        steps = listOf(
            ProjectStepContent("1/5 — Crie o número secreto", "Crie uma variável chamada numero_secreto com um número.", "Exemplo: numero_secreto = 7", "numero_secreto = 7"),
            ProjectStepContent("2/5 — Peça o palpite", "Use input() para pedir um palpite e guarde a resposta em uma variável.", "Exemplo: palpite = int(input(\"Digite seu palpite: \"))", "palpite = int(input(\"Digite seu palpite: \"))"),
            ProjectStepContent("3/5 — Compare os números", "Use if para verificar se o palpite é igual ao número secreto.", "Exemplo: if palpite == numero_secreto:", "if palpite == numero_secreto:\n    print(\"Acertou!\")"),
            ProjectStepContent("4/5 — Dê uma dica", "Use elif ou else para informar se o palpite é maior ou menor.", "Use print() para mostrar a dica.", "elif palpite > numero_secreto:\n    print(\"Muito alto!\")\nelse:\n    print(\"Muito baixo!\")"),
            ProjectStepContent("5/5 — Conte tentativas", "Crie tentativas e aumente esse contador quando o jogador tentar.", "Comece com tentativas = 0 e aumente com tentativas = tentativas + 1.", "tentativas = 0\n\nwhile tentativas < 5:\n    # seu jogo aqui\n    tentativas = tentativas + 1")
        )
    )

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

    fun exercisesFor(language: String): List<ExerciseContent> =
        if (language == "🐍  Python") pythonExercises else emptyList()

    fun projectFor(language: String, projectId: Int = 1): ProjectContent? =
        if (language == "🐍  Python" && projectId == 1) pythonProject else null

}
