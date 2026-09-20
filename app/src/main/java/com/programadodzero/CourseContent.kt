package com.programadodzero

/**
 * Modelo de conteúdo pedagógico da aplicação.
 *
 * O conteúdo fica separado das Activities para que possamos adicionar
 * centenas de aulas sem transformar a interface em um arquivo gigante.
 */
data class LessonContent(
    val id: String,
    val title: String,
    val body: String,
    val code: String
)

object ContentRepository {
    private val pythonLessons = listOf(
        LessonContent(
            id = "python-01",
            title = "Aula 1 — O que é programação?",
            body = "Programar é dar instruções claras para o computador.",
            code = "print(\"Olá, mundo!\")"
        ),
        LessonContent(
            id = "python-02",
            title = "Aula 2 — Variáveis",
            body = "Variáveis guardam informações que podemos usar depois.",
            code = "nome = \"Ana\"\nidade = 20\nprint(nome)"
        ),
        LessonContent(
            id = "python-03",
            title = "Aula 3 — Tipos de dados",
            body = "Texto, números, decimais e booleanos representam tipos diferentes.",
            code = "nome = \"Ana\"\nidade = 20\naltura = 1.65\naluno = True"
        ),
        LessonContent(
            id = "python-04",
            title = "Aula 4 — Condições",
            body = "Use if para tomar decisões.",
            code = "idade = 20\n\nif idade >= 18:\n    print(\"Maior de idade\")"
        ),
        LessonContent(
            id = "python-05",
            title = "Aula 5 — Repetições",
            body = "Use for para repetir tarefas.",
            code = "for numero in range(5):\n    print(numero)"
        ),
        LessonContent(
            id = "python-06",
            title = "Aula 6 — Funções",
            body = "Funções agrupam instruções reutilizáveis.",
            code = "def saudacao(nome):\n    return \"Olá, \" + nome"
        ),
        LessonContent(
            id = "python-07",
            title = "Aula 7 — Listas",
            body = "Listas guardam vários valores.",
            code = "frutas = [\"maçã\", \"banana\", \"uva\"]"
        ),
        LessonContent(
            id = "python-08",
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

    fun lessonsFor(language: String): List<LessonContent> =
        if (language == "🐍  Python") pythonLessons else genericLessons
}
