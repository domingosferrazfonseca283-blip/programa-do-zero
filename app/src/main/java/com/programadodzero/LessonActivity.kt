package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class LessonActivity : Activity() {

    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
    }

    private data class Lesson(val title: String, val body: String, val code: String)
    private lateinit var progress: TextView
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var code: TextView
    private lateinit var nextButton: Button
    private var currentLesson = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        val level = intent.getStringExtra(EXTRA_LEVEL) ?: "Nível"
        val lessons = createLessons(language)

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 40, 32, 32)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val header = TextView(this).apply {
            text = "📚 $language • $level"
            textSize = 21f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        progress = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 14, 0, 22)
        }

        title = TextView(this).apply {
            textSize = 27f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        body = TextView(this).apply {
            textSize = 18f
            setTextColor(Color.LTGRAY)
            setPadding(0, 18, 0, 18)
        }

        code = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.WHITE)
            setPadding(20, 18, 20, 18)
            setBackgroundColor(Color.rgb(30, 41, 59))
            typeface = Typeface.MONOSPACE
        }

        nextButton = Button(this).apply {
            textSize = 17f
            isAllCaps = false
        }

        nextButton.setOnClickListener {
            if (currentLesson < lessons.lastIndex) {
                currentLesson++
                showLesson(lessons)
            } else {
                nextButton.text = "Trilha concluída ✓"
                nextButton.isEnabled = false
                progress.text = "5 de 5 aulas • 100% concluído"
            }
        }

        val backButton = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(header)
        screen.addView(progress)
        screen.addView(title)
        screen.addView(body, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(code, LinearLayout.LayoutParams(-1, -2))
        screen.addView(nextButton, LinearLayout.LayoutParams(-1, 65))
        screen.addView(backButton, LinearLayout.LayoutParams(-1, 60))

        setContentView(screen)
        showLesson(lessons)
    }

    private fun createLessons(language: String): List<Lesson> {
        val examples = mapOf(
            "🐍  Python" to "nome = \"Ana\"\nprint(nome)",
            "🌐  JavaScript" to "const nome = \"Ana\";\nconsole.log(nome);",
            "🔷  TypeScript" to "const nome: string = \"Ana\";\nconsole.log(nome);",
            "☕  Java" to "String nome = \"Ana\";\nSystem.out.println(nome);",
            "⚙️  C" to "char nome[] = \"Ana\";\nprintf(\"%s\\n\", nome);",
            "🚀  C++" to "string nome = \"Ana\";\ncout << nome << endl;",
            "🦀  Rust" to "let nome = \"Ana\";\nprintln!(\"{}\", nome);",
            "🐹  Go" to "nome := \"Ana\"\nfmt.Println(nome)"
        )
        val example = examples[language] ?: "nome = \"Ana\""

        return listOf(
            Lesson("Aula 1 — O que é programação?", "Programar é escrever instruções para que um computador realize tarefas. Nesta trilha você vai aprender conceitos e praticar até criar projetos.", example),
            Lesson("Aula 2 — Variáveis", "Uma variável guarda um valor que o programa pode usar. Aqui começamos com um nome armazenado em uma variável.", example),
            Lesson("Aula 3 — Dados e tipos", "Programas trabalham com diferentes tipos de dados, como texto, números e valores booleanos. Entender os tipos ajuda a escrever código correto.", "texto = \"Olá\"\nnumero = 10\nativo = true"),
            Lesson("Aula 4 — Decisões", "Uma condição permite que o programa escolha o que fazer de acordo com uma situação. O conceito de if aparece em praticamente toda linguagem.", "if (idade >= 18) {\n    // executar uma ação\n}"),
            Lesson("Aula 5 — Repetições", "Laços de repetição permitem executar uma tarefa várias vezes sem duplicar o mesmo código.", "for (item in itens) {\n    // processar item\n}")
        )
    }

    private fun showLesson(lessons: List<Lesson>) {
        val lesson = lessons[currentLesson]
        val number = currentLesson + 1
        progress.text = "Aula $number de ${lessons.size}  •  ${((number - 1) * 100 / lessons.size)}% concluído"
        title.text = lesson.title
        body.text = lesson.body
        code.text = "Exemplo:\n\n${lesson.code}"
        nextButton.text = if (currentLesson == lessons.lastIndex) "Concluir trilha ✓" else "Próxima aula →"
    }
}
