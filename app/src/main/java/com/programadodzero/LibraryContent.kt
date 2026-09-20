package com.programadodzero

data class LibraryChapter(val id: String, val title: String, val content: String, val relatedLessonId: String? = null)
data class LibraryBook(val id: String, val language: String, val title: String, val description: String, val chapters: List<LibraryChapter>, val source: String = "Conteúdo original do Programa do Zero", val license: String = "Conteúdo original")

object LibraryRepository {
    private fun book(id: String, language: String, title: String, description: String, topics: List<String>): LibraryBook {
        val pythonLessonIds = listOf(
            "python-01", "python-02", "python-03", "python-04",
            "python-05", "python-06", "python-07", "python-09",
            "python-10", "python-11", "python-12", "python-13",
            "python-14", "python-19", "python-18", "python-20"
        )
        val chapters = topics.mapIndexed { index, topic ->
            LibraryChapter(
                id + "-ch-" + (index + 1),
                "Capítulo " + (index + 1) + " — " + topic,
                chapterText(language, topic),
                if (language == "🐍  Python" && index < pythonLessonIds.size) pythonLessonIds[index] else null
            )
        }
        return LibraryBook(id, language, title, description, chapters, "Conteúdo original do Programa do Zero", "Conteúdo original")
    }

    private fun openResource(id: String, language: String, title: String, description: String, source: String, license: String): LibraryBook {
        val content = "$title\\n\\n$description\\n\\nFonte: $source\\nLicença: $license\\n\\nEste recurso é catalogado pela Biblioteca Offline. O conteúdo completo só deve ser incorporado ao aplicativo quando a licença e a forma de redistribuição permitirem. Use esta ficha para identificar a obra e seus termos de uso."
        return LibraryBook(
            id,
            language,
            title,
            description,
            listOf(LibraryChapter(id + "-info", "Sobre este recurso aberto", content)),
            source,
            license
        )
    }

    private fun chapterText(language: String, topic: String): String {
        if (language == "🐍  Python") {
            val texts = mapOf(
                "Pensamento computacional e sintaxe" to "Transforme problemas em entradas, processamento e saídas. Divida a solução em passos pequenos e testáveis. Em Python, comece com instruções simples e avance para estruturas de controle.",
                "Variáveis e tipos de dados" to "Variáveis guardam valores. Os tipos iniciais mais importantes são int, float, str e bool. Use nomes claros e escolha o tipo adequado ao dado.",
                "Entrada, saída e conversões" to "input() lê texto. Para trabalhar com números, use conversões como int() e float(). Valide entradas quando elas puderem ser inválidas.",
                "Condições e tomada de decisão" to "if, elif e else permitem escolher caminhos. Operadores como ==, !=, <, >, <= e >= expressam comparações.",
                "Repetições e laços" to "for percorre sequências e range cria intervalos. while repete enquanto uma condição for verdadeira. Controle a condição para evitar laços infinitos.",
                "Funções e parâmetros" to "Funções agrupam responsabilidades. Parâmetros recebem dados e return devolve resultados. Prefira funções pequenas e reutilizáveis.",
                "Listas e operações" to "Listas armazenam sequências. Índices começam em zero; append adiciona itens e len informa o tamanho. Pratique criação, leitura e alteração.",
                "Dicionários e dados estruturados" to "Dicionários representam pares chave-valor e são úteis para dados com propriedades nomeadas. Pense em cada chave como um campo.",
                "Strings e processamento de texto" to "Strings representam texto. upper(), lower(), strip() e len() são operações fundamentais para normalizar e analisar entradas.",
                "Erros e tratamento de exceções" to "Erros fazem parte de programas reais. try/except permite tratar situações previsíveis e apresentar uma recuperação clara ao usuário.",
                "Arquivos e persistência" to "Arquivos permitem preservar dados entre execuções. Use with open(...) para controlar o recurso e trate falhas de leitura e escrita.",
                "Classes e objetos" to "Classes modelam dados e comportamentos. __init__ inicializa objetos e self representa a instância atual. Use orientação a objetos quando ela simplificar o domínio.",
                "Organização de sistemas" to "Separe entrada, regras, persistência e apresentação. Responsabilidades bem definidas tornam sistemas maiores mais fáceis de testar e manter.",
                "Algoritmos e complexidade" to "Um algoritmo é uma sequência de passos para resolver um problema. Observe como o custo cresce com o tamanho da entrada e compare estratégias.",
                "Testes e qualidade" to "Teste casos normais, limites e entradas inválidas. Funções pequenas e previsíveis são mais fáceis de verificar automaticamente.",
                "Projeto profissional" to "Um projeto completo combina requisitos, organização, implementação, validação, testes e documentação. Comece pequeno e evolua por etapas."
            )
            val explanation = texts[topic] ?: "Estude o conceito, pratique com exemplos pequenos e depois aplique-o em um projeto."
            return "$topic\n\n$explanation\n\nComo estudar:\n1. Reescreva um exemplo.\n2. Modifique uma parte.\n3. Teste diferentes entradas.\n4. Explique o resultado com suas próprias palavras.\n\nPrática: crie um pequeno programa que use este conceito e valide pelo menos dois casos diferentes."
        }
        return "$topic\n\nEste capítulo apresenta o conceito de forma progressiva. Entenda o problema que a técnica resolve, observe exemplos pequenos e pratique antes de avançar.\n\nPrática: escreva um exemplo simples, altere uma parte e compare os resultados."
    }

    val openResources: List<LibraryBook> = listOf(
        openResource(
            "oer-python-foundations",
            "🐍  Python",
            "Programming Foundations — TU Delft",
            "Livro aberto de fundamentos de programação em Python, com exercícios e progressão prática.",
            "Nikolina Šoštarić / TU Delft OPEN Books, 2nd edition (2025)",
            "CC BY 4.0"
        ),
        openResource(
            "oer-exploring-cs",
            "Python",
            "Exploring Computer Science",
            "Livro introdutório aberto de ciência da computação com Python.",
            "Ian Finlayson",
            "CC BY-NC-SA 4.0"
        ),
        openResource(
            "oer-think-python",
            "Python",
            "Think Python 2e",
            "Livro aberto sobre pensamento computacional e Python.",
            "Allen Downey",
            "CC BY-NC-SA 3.0"
        )
    )

    val books: List<LibraryBook> = listOf(
        book("python-fundamentos", "🐍  Python", "Python do Zero ao Código", "Livro introdutório original para acompanhar a trilha de Python.", listOf("Pensamento computacional e sintaxe", "Variáveis e tipos de dados", "Entrada, saída e conversões", "Condições e tomada de decisão", "Repetições e laços", "Funções e parâmetros", "Listas e operações", "Dicionários e dados estruturados", "Strings e processamento de texto", "Erros e tratamento de exceções", "Arquivos e persistência", "Classes e objetos", "Organização de sistemas", "Algoritmos e complexidade", "Testes e qualidade", "Projeto profissional")),
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

    fun allBooks(): List<LibraryBook> = books + openResources

    fun search(query: String): List<LibraryBook> {
        val q = query.trim().lowercase()
        if (q.isBlank()) return allBooks()
        return allBooks().filter {
            it.title.lowercase().contains(q) ||
            it.language.lowercase().contains(q) ||
            it.description.lowercase().contains(q) ||
            it.chapters.any { chapter -> chapter.title.lowercase().contains(q) }
        }
    }

    fun find(bookId: String): LibraryBook? = allBooks().firstOrNull { it.id == bookId }
}
