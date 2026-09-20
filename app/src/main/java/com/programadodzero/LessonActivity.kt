package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.content.Intent
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
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtra(ExerciseActivity.EXTRA_LANGUAGE, language)
            intent.putExtra(ExerciseActivity.EXTRA_LEVEL, level)
            intent.putExtra(ExerciseActivity.EXTRA_LESSON, currentLesson)
            startActivity(intent)
        }

        nextButton.setOnLongClickListener {
            if (currentLesson < lessons.lastIndex) {
                currentLesson++
                showLesson(lessons)
            } else {
                nextButton.text = "Trilha concluída ✓"
                nextButton.isEnabled = false
                progress.text = "5 de 5 aulas • 100% concluído"
            }
            true
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
        val lessonsByLanguage = mapOf(
            "🐍  Python" to listOf(
                Lesson("Aula 1 — O que é programação?", "Programar é dar instruções claras para o computador. Vamos começar pensando como um programador: dividir um problema grande em passos pequenos.", "print(\"Olá, mundo!\")"),
                Lesson("Aula 2 — Variáveis", "Variáveis guardam informações que podemos usar depois. Pense nelas como caixas com nomes.", "nome = \"Ana\"\nidade = 20\nprint(nome)"),
                Lesson("Aula 3 — Tipos de dados", "Texto, números inteiros, números decimais e valores booleanos representam tipos diferentes de informação.", "nome = \"Ana\"\nidade = 20\naltura = 1.65\naluno = True"),
                Lesson("Aula 4 — Condições", "Programas precisam tomar decisões. Em Python, usamos if para executar algo somente quando uma condição é verdadeira.", "idade = 20\n\nif idade >= 18:\n    print(\"Maior de idade\")"),
                Lesson("Aula 5 — Repetições", "Quando precisamos repetir uma tarefa, usamos laços. O for permite percorrer uma sequência passo a passo.", "for numero in range(5):\n    print(numero)"),
                Lesson("Aula 6 — Funções", "Funções agrupam instruções em uma unidade reutilizável. Elas ajudam a organizar programas maiores.", "def saudacao(nome):\n    return \"Olá, \" + nome\n\nprint(saudacao(\"Ana\"))"),
                Lesson("Aula 7 — Listas", "Listas permitem guardar vários valores em uma única estrutura e percorrê-los no programa.", "frutas = [\"maçã\", \"banana\", \"uva\"]\nfor fruta in frutas:\n    print(fruta)"),
                Lesson("Aula 8 — Primeiro projeto", "Agora vamos juntar variáveis, entrada, condições e funções para criar um pequeno programa útil.", "nome = input(\"Seu nome: \")\nprint(\"Olá, \" + nome + \"!\")")
            )
        )

        val generic = listOf(
            Lesson("Aula 1 — O que é programação?", "Programar é escrever instruções para que um computador realize tarefas. Vamos aprender passo a passo.", "print(\"Olá, mundo!\")"),
            Lesson("Aula 2 — Variáveis", "Uma variável guarda um valor que o programa pode usar.", "nome = \"Ana\""),
            Lesson("Aula 3 — Dados e tipos", "Programas trabalham com diferentes tipos de dados, como texto, números e valores booleanos.", "texto = \"Olá\"\nnumero = 10\nativo = true"),
            Lesson("Aula 4 — Decisões", "Condições permitem que o programa escolha o que fazer.", "if (idade >= 18) {\n    // executar uma ação\n}"),
            Lesson("Aula 5 — Repetições", "Laços permitem repetir tarefas sem duplicar código.", "for (item in itens) {\n    // processar item\n}")
        )

        return lessonsByLanguage[language] ?: generic
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
