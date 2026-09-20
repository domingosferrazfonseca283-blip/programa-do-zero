package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class PracticeCodingActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_LESSON = "lesson"
    }

    private data class Challenge(val title: String, val instruction: String, val starter: String, val success: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "🐍  Python"
        val level = intent.getStringExtra(EXTRA_LEVEL) ?: "Prática"
        val lesson = intent.getIntExtra(EXTRA_LESSON, 0)
        val challenge = pythonChallenges()[lesson.coerceIn(0, 7)]

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 30, 24, 24)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        val header = TextView(this).apply {
            text = "⌨️ Prática de código ${lesson + 1}/8 • $language"
            textSize = 20f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }
        val instruction = TextView(this).apply {
            text = "${challenge.title}\n\n${challenge.instruction}"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            setPadding(0, 16, 0, 16)
        }
        val editor = EditText(this).apply {
            setText(challenge.starter)
            textSize = 17f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(30, 41, 59))
            typeface = Typeface.MONOSPACE
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            setPadding(16, 16, 16, 16)
            minLines = 7
        }
        val feedback = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 14, 0, 10)
        }
        val check = Button(this).apply {
            text = "▶ Verificar código"
            textSize = 17f
            isAllCaps = false
        }
        val next = Button(this).apply {
            text = if (lesson < 7) "Próxima aula →" else "🏆 Concluir linguagem"
            isAllCaps = false
            isEnabled = false
        }

        check.setOnClickListener {
            val result = validatePython(lesson, editor.text.toString().trim())
            if (result) {
                val firstTime = ProgressManager.completeExercise(this, language, lesson)
                ProgressManager.completeLesson(this, language, lesson)
                feedback.text = if (firstTime) "✅ Muito bem! ${challenge.success}\n\n+50 XP" else "✅ Código correto! ${challenge.success}"
                check.isEnabled = false
                next.isEnabled = true
            } else {
                feedback.text = "❌ Ainda não.\n\nLeia o objetivo novamente, altere seu código e tente outra vez.\n\n💡 Dica: use o conceito aprendido nesta aula."
            }
        }

        next.setOnClickListener {
            if (lesson < 7) {
                startActivity(android.content.Intent(this, PracticeCodingActivity::class.java).apply {
                    putExtra(EXTRA_LANGUAGE, language)
                    putExtra(EXTRA_LEVEL, level)
                    putExtra(EXTRA_LESSON, lesson + 1)
                })
                finish()
            } else finish()
        }

        val back = Button(this).apply {
            text = "← Voltar à aula"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(header)
        screen.addView(instruction)
        screen.addView(editor, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(check, LinearLayout.LayoutParams(-1, 62))
        screen.addView(feedback)
        screen.addView(next, LinearLayout.LayoutParams(-1, 60))
        screen.addView(back, LinearLayout.LayoutParams(-1, 58))
        setContentView(screen)
    }

    private fun validatePython(lesson: Int, code: String): Boolean {
        if (code.isBlank()) return false
        return when (lesson) {
            0 -> code.contains("print(")
            1 -> code.contains("=") && (code.contains("nome") || code.contains("idade"))
            2 -> code.contains("=") && (code.contains("\"") || code.contains("'")) && code.any { it.isDigit() }
            3 -> code.contains("if ") && code.contains(":")
            4 -> code.contains("for ") && code.contains("range(")
            5 -> code.contains("def ") && code.contains(":") && code.contains("return")
            6 -> code.contains("[") && code.contains("]")
            7 -> code.contains("input(") && code.contains("print(") && code.contains("if ") && code.contains(":") && code.contains("=")
            else -> false
        }
    }

    private fun pythonChallenges(): List<Challenge> = listOf(
        Challenge("Aula 1 — Mostre uma mensagem", "Escreva um programa que mostre Olá, mundo! usando print().", "print(\"Olá, mundo!\")", "Você acabou de escrever seu primeiro programa."),
        Challenge("Aula 2 — Crie uma variável", "Crie uma variável chamada nome e coloque um nome dentro dela.", "nome = \"Ana\"\nprint(nome)", "Variáveis permitem guardar informações."),
        Challenge("Aula 3 — Trabalhe com dados", "Crie uma variável de texto e outra com um número.", "nome = \"Ana\"\nidade = 20", "Agora você consegue guardar diferentes tipos de dados."),
        Challenge("Aula 4 — Tome uma decisão", "Use if para mostrar uma mensagem quando idade for 18 ou maior.", "idade = 20\n\nif idade >= 18:\n    print(\"Maior de idade\")", "Você ensinou o programa a tomar uma decisão."),
        Challenge("Aula 5 — Repita uma tarefa", "Use for e range() para mostrar números de 0 a 4.", "for numero in range(5):\n    print(numero)", "Laços permitem repetir tarefas sem copiar o código."),
        Challenge("Aula 6 — Crie uma função", "Crie uma função que receba um nome e retorne uma saudação.", "def saudacao(nome):\n    return \"Olá, \" + nome", "Funções ajudam a organizar e reutilizar código."),
        Challenge("Aula 7 — Use uma lista", "Crie uma lista com pelo menos dois itens.", "frutas = [\"maçã\", \"banana\"]\nprint(frutas)", "Listas permitem trabalhar com vários valores juntos."),
        Challenge("Aula 8 — Primeiro projeto", "Crie um pequeno programa: peça um valor com input(), guarde em uma variável, use if para tomar uma decisão e mostre um resultado com print().", "nome = input(\"Seu nome: \")\n\nif nome:\n    print(\"Olá, \" + nome + \"!\")", "Você juntou entrada, variável, condição e saída em um pequeno projeto.")
    )
}
