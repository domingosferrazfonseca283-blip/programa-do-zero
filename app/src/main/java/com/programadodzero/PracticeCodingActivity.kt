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
        const val EXTRA_LESSON_ID = "lesson_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "🐍  Python"
        val level = intent.getStringExtra(EXTRA_LEVEL) ?: "Prática"
        val lessonIndex = intent.getIntExtra(EXTRA_LESSON, 0)
        val lessonId = intent.getStringExtra(EXTRA_LESSON_ID)
        val challenges = ContentRepository.exercisesFor(language)
        if (challenges.isEmpty()) {
            finish()
            return
        }
        val challenge = if (lessonId != null) challenges.firstOrNull { it.id == lessonId } ?: run { finish(); return } else challenges[lessonIndex.coerceIn(0, challenges.lastIndex)]
        val lesson = challenges.indexOf(challenge)

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 30, 24, 24)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        val header = TextView(this).apply {
            text = "⌨️ Prática de código ${lesson + 1}/${challenges.size} • $language"
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
            text = if (lesson < challenges.lastIndex) "Próxima aula →" else "🏆 Concluir linguagem"
            isAllCaps = false
            isEnabled = false
        }

        check.setOnClickListener {
            val codeText = editor.text.toString().trim()
            val result = validatePython(challenge.id, codeText)
            if (result) {
                val firstTime = ProgressManager.completeExercise(this, language, challenge.id)
                ProgressManager.completeLesson(this, language, challenge.id)
                feedback.text = if (firstTime) "✅ Muito bem! ${challenge.success}\n\n+50 XP" else "✅ Código correto! ${challenge.success}"
                check.isEnabled = false
                next.isEnabled = true
            } else {
                feedback.text = pythonHint(challenge.id, codeText)
            }
        }

        next.setOnClickListener {
            if (lesson < challenges.lastIndex) {
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

    private fun validatePython(exerciseId: String, code: String): Boolean =
        ChallengeValidator.validate(exerciseId, code)

    private fun pythonHint(exerciseId: String, code: String): String {
        if (code.isBlank()) return "❌ O editor está vazio.\\n\\n💡 Comece pelo exemplo da aula e altere uma parte dele."
        return when (exerciseId) {
            "python-01" -> if (!code.contains("print(")) "❌ Você ainda não usou print().\\n\\n💡 Use print(...) para mostrar uma mensagem." else "❌ Revise a escrita do print().\\n\\n💡 Compare seu código com o exemplo da aula."
            "python-02" -> if (!code.contains("=")) "❌ Falta criar uma variável.\\n\\n💡 Em Python, usamos = para guardar um valor." else "❌ A variável precisa ter um nome como nome ou idade.\\n\\n💡 Tente: nome = \"Ana\""
            "python-03" -> if (!code.any { it.isDigit() }) "❌ Falta um número.\\n\\n💡 Crie uma variável como idade = 20." else "❌ Você precisa trabalhar com texto e número.\\n\\n💡 Use aspas para texto e um número sem aspas."
            "python-04" -> if (!code.contains("if ")) "❌ Falta uma condição com if.\\n\\n💡 Comece com: if idade >= 18:" else "❌ Parece que a condição está incompleta.\\n\\n💡 Em Python, a linha do if termina com :."
            "python-05" -> if (!code.contains("range(")) "❌ Falta range().\\n\\n💡 Use for numero in range(5): para repetir 5 vezes." else "❌ Revise o laço for.\\n\\n💡 Ele precisa ter for, range() e :."
            "python-06" -> if (!code.contains("def ")) "❌ Falta criar a função com def.\\n\\n💡 Comece com def saudacao(nome):" else if (!code.contains("return")) "❌ A função precisa retornar um resultado.\\n\\n💡 Use return dentro da função." else "❌ Revise a estrutura da função.\\n\\n💡 A linha def precisa terminar com :."
            "python-07" -> when {
                !code.contains("[") || !code.contains("]") -> "❌ Falta criar uma lista.\\n\\n💡 Use colchetes [ ] para colocar vários itens juntos."
                !code.contains(",") -> "❌ A lista precisa ter pelo menos dois itens.\\n\\n💡 Separe os itens com vírgula."
                !code.contains("[1]") -> "❌ Agora mostre o segundo item da lista.\\n\\n💡 Se a lista se chama frutas, use frutas[1]."
                else -> "❌ A lista precisa ser criada e o segundo item precisa ser mostrado."
            }
            "python-08" -> if (!code.contains("input(")) "❌ Falta input().\\n\\n💡 Use input() para pedir uma informação ao usuário." else if (!code.contains("if ")) "❌ Falta a decisão do projeto.\\n\\n💡 Use if para verificar a informação recebida." else if (!code.contains("print(")) "❌ Falta mostrar o resultado.\\n\\n💡 Use print() para apresentar uma mensagem." else "❌ Revise a estrutura do projeto.\\n\\n💡 Você precisa juntar input(), variável, if e print()."
            else -> "❌ Revise o objetivo da aula e tente novamente.\\n\\n💡 Use o exemplo como ponto de partida."
        }
    }

}
