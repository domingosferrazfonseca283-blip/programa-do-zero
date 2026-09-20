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
            val codeText = editor.text.toString().trim()
            val result = validatePython(lesson, codeText)
            if (result) {
                val firstTime = ProgressManager.completeExercise(this, language, lesson)
                ProgressManager.completeLesson(this, language, lesson)
                feedback.text = if (firstTime) "✅ Muito bem! ${challenge.success}\n\n+50 XP" else "✅ Código correto! ${challenge.success}"
                check.isEnabled = false
                next.isEnabled = true
            } else {
                feedback.text = pythonHint(lesson, codeText)
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
            } else {
                startActivity(android.content.Intent(this, ProjectActivity::class.java).apply {
                    putExtra(EXTRA_LANGUAGE, language)
                })
                finish()
            }
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

    private fun validatePython(lesson: Int, code: String): Boolean =
        ChallengeValidator.validate(lesson, code)

    private fun pythonHint(lesson: Int, code: String): String {
        if (code.isBlank()) return "❌ O editor está vazio.\\n\\n💡 Comece pelo exemplo da aula e altere uma parte dele."
        return when (lesson) {
            0 -> if (!code.contains("print(")) "❌ Você ainda não usou print().\\n\\n💡 Use print(...) para mostrar uma mensagem." else "❌ Revise a escrita do print().\\n\\n💡 Compare seu código com o exemplo da aula."
            1 -> if (!code.contains("=")) "❌ Falta criar uma variável.\\n\\n💡 Em Python, usamos = para guardar um valor." else "❌ A variável precisa ter um nome como nome ou idade.\\n\\n💡 Tente: nome = \"Ana\""
            2 -> if (!code.any { it.isDigit() }) "❌ Falta um número.\\n\\n💡 Crie uma variável como idade = 20." else "❌ Você precisa trabalhar com texto e número.\\n\\n💡 Use aspas para texto e um número sem aspas."
            3 -> if (!code.contains("if ")) "❌ Falta uma condição com if.\\n\\n💡 Comece com: if idade >= 18:" else "❌ Parece que a condição está incompleta.\\n\\n💡 Em Python, a linha do if termina com :."
            4 -> if (!code.contains("range(")) "❌ Falta range().\\n\\n💡 Use for numero in range(5): para repetir 5 vezes." else "❌ Revise o laço for.\\n\\n💡 Ele precisa ter for, range() e :."
            5 -> if (!code.contains("def ")) "❌ Falta criar a função com def.\\n\\n💡 Comece com def saudacao(nome):" else if (!code.contains("return")) "❌ A função precisa retornar um resultado.\\n\\n💡 Use return dentro da função." else "❌ Revise a estrutura da função.\\n\\n💡 A linha def precisa terminar com :."
            6 -> "❌ Sua lista ainda não está completa.\\n\\n💡 Use colchetes [ ] e coloque pelo menos dois itens dentro."
            7 -> if (!code.contains("input(")) "❌ Falta input().\\n\\n💡 Use input() para pedir uma informação ao usuário." else if (!code.contains("if ")) "❌ Falta a decisão do projeto.\\n\\n💡 Use if para verificar a informação recebida." else if (!code.contains("print(")) "❌ Falta mostrar o resultado.\\n\\n💡 Use print() para apresentar uma mensagem." else "❌ Revise a estrutura do projeto.\\n\\n💡 Você precisa juntar input(), variável, if e print()."
            else -> "❌ Revise o objetivo da aula e tente novamente.\\n\\n💡 Use o exemplo como ponto de partida."
        }
    }

    private fun pythonChallenges(): List<Challenge> = listOf(
        Challenge("Aula 1 — Mostre uma mensagem", "Escreva um programa que mostre Olá, mundo! usando print().", "print(\"Olá, mundo!\")", "Você acabou de escrever seu primeiro programa."),
        Challenge("Aula 2 — Crie uma variável", "Crie uma variável chamada nome e coloque um nome dentro dela.", "nome = \"Ana\"\nprint(nome)", "Variáveis permitem guardar informações."),
        Challenge("Aula 3 — Trabalhe com dados", "Crie uma variável de texto e outra com um número.", "nome = \"Ana\"\nidade = 20", "Agora você consegue guardar diferentes tipos de dados."),
        Challenge("Aula 4 — Tome uma decisão", "Peça a idade com input(), transforme a resposta em número e use if/else para mostrar mensagens diferentes para menor e maior de idade.", "idade = int(input(\"Digite sua idade: \"))\n\nif idade >= 18:\n    print(\"Maior de idade\")\nelse:\n    print(\"Menor de idade\")", "Agora o programa recebe uma informação e toma uma decisão com base nela."),
        Challenge("Aula 5 — Repita uma tarefa", "Use for e range() para mostrar uma sequência de pelo menos três números em ordem crescente.", "for numero in range(5):\n    print(numero)", "Agora você consegue repetir uma tarefa sem copiar o código várias vezes."),
                Challenge("Aula 6 — Crie uma função", "Crie uma função que receba um nome e retorne uma saudação.", "def saudacao(nome):\n    return \"Olá, \" + nome\n\nprint(saudacao(\"Ana\"))", "Funções ajudam a organizar e reutilizar código."),
        Challenge("Aula 7 — Use uma lista", "Crie uma lista com pelo menos dois itens.", "frutas = [\"maçã\", \"banana\"]\nprint(frutas)", "Listas permitem trabalhar com vários valores juntos."),
        Challenge("Aula 8 — Primeiro projeto", "Crie um pequeno programa: peça um valor com input(), guarde em uma variável, use if para tomar uma decisão e mostre um resultado com print().", "nome = input(\"Seu nome: \")\n\nif nome:\n    print(\"Olá, \" + nome + \"!\")", "Você juntou entrada, variável, condição e saída em um pequeno projeto.")
    )
}
