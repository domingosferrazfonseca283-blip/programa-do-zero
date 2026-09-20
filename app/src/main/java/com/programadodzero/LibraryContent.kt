package com.programadodzero

data class LibraryChapter(val id: String, val title: String, val content: String)
data class LibraryBook(val id: String, val language: String, val title: String, val description: String, val chapters: List<LibraryChapter>)

object LibraryRepository {
    private fun book(id: String, language: String, title: String, description: String, topics: List<String>): LibraryBook {
        val chapters = topics.mapIndexed { index, topic ->
            LibraryChapter(id + "-ch-" + (index + 1), "Capítulo " + (index + 1) + " — " + topic, chapterText(language, topic))
        }
        return LibraryBook(id, language, title, description, chapters)
    }

    private fun chapterText(language: String, topic: String): String = """
        $topic

        Este capítulo faz parte de um livro didático original criado para o Programa do Zero. A ideia é aprender o conceito, observar exemplos e depois voltar ao curso para praticar.

        O que você deve entender:
        • Qual problema o conceito resolve.
        • Como ele aparece em programas reais.
        • Como ler e explicar um exemplo.
        • Quando vale a pena usar essa técnica.

        Exemplo conceitual em $language:
        Comece com um problema pequeno, identifique os dados de entrada, descreva o processamento e defina a saída esperada. Em seguida, transforme cada passo em código e teste com casos diferentes.

        Dica de estudo:
        Não tente decorar tudo. Leia o capítulo, escreva um pequeno exemplo no editor do aplicativo e depois explique com suas próprias palavras o que aconteceu.

        Próximo passo:
        Volte para a trilha de aulas da linguagem e procure uma aula relacionada a este assunto. A leitura complementa a prática.
    """.trimIndent()

    val books: List<LibraryBook> = listOf(
        book("python-fundamentos", "🐍  Python", "Python do Zero ao Código", "Livro introdutório original para acompanhar a trilha de Python.", listOf("Pensamento computacional e sintaxe", "Variáveis, tipos e decisões", "Laços, funções e estruturas de dados", "Projetos, testes e organização")),
        book("javascript-fundamentos", "🌐  JavaScript", "JavaScript do Zero", "Livro introdutório original sobre a linguagem e seus fundamentos.", listOf("Sintaxe, valores e variáveis", "Condições, laços e funções", "Objetos, arrays e módulos", "Projetos e código para a web")),
        book("typescript-fundamentos", "🔷  TypeScript", "TypeScript do Zero", "Introdução prática a tipos e desenvolvimento seguro.", listOf("Tipos e inferência", "Funções e interfaces", "Objetos, generics e organização", "Projetos TypeScript")),
        book("java-fundamentos", "☕  Java", "Java do Zero", "Fundamentos da linguagem Java para iniciantes.", listOf("Sintaxe e tipos", "Condições, laços e métodos", "Classes e orientação a objetos", "Projetos e boas práticas")),
        book("c-fundamentos", "⚙️  C", "C do Zero", "Fundamentos de programação de baixo nível.", listOf("Sintaxe e memória", "Condições, laços e funções", "Arrays, ponteiros e structs", "Projetos e depuração")),
        book("cpp-fundamentos", "🚀  C++", "C++ do Zero", "Introdução moderna à linguagem C++.", listOf("Sintaxe e tipos", "Funções e estruturas", "Classes e orientação a objetos", "STL e projetos")),
        book("rust-fundamentos", "🦀  Rust", "Rust do Zero", "Introdução à linguagem Rust e seu modelo de segurança.", listOf("Sintaxe, variáveis e mutabilidade", "Ownership e borrowing", "Structs, enums e traits", "Projetos e organização")),
        book("go-fundamentos", "🐹  Go", "Go do Zero", "Fundamentos de Go com foco em programas simples e claros.", listOf("Sintaxe e tipos", "Funções e estruturas", "Interfaces e concorrência", "Projetos e ferramentas"))
        ,book("algoritmos-fundamentos", "🧠  Algoritmos", "Algoritmos e Estruturas de Dados", "Livro didático original sobre resolução de problemas, complexidade e estruturas de dados.", listOf("Pensamento algorítmico", "Busca e ordenação", "Pilhas, filas e listas", "Árvores, grafos e complexidade"))
        ,book("git-fundamentos", "🌿  Git", "Git do Zero", "Livro didático original para aprender controle de versão e colaboração.", listOf("Repositórios e commits", "Branches e merges", "Histórico e recuperação", "Fluxos de colaboração"))
        ,book("banco-dados", "🗄️  Banco de dados", "Bancos de Dados do Zero", "Livro didático original sobre modelagem e armazenamento de dados.", listOf("Dados, tabelas e relacionamentos", "SQL e consultas", "Chaves e normalização", "Transações e projetos"))
        ,book("ciberseguranca", "🛡️  Cibersegurança", "Cibersegurança do Zero", "Introdução defensiva a segurança, privacidade e boas práticas.", listOf("Princípios de segurança", "Autenticação e senhas", "Redes e ameaças", "Defesa, logs e resposta a incidentes"))
    )

    fun search(query: String): List<LibraryBook> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return books
        return books.filter {
            it.title.lowercase().contains(q) ||
            it.language.lowercase().contains(q) ||
            it.description.lowercase().contains(q) ||
            it.chapters.any { chapter -> chapter.title.lowercase().contains(q) }
        }
    }

    fun find(bookId: String): LibraryBook? = books.firstOrNull { it.id == bookId }
}
