package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ExerciseActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_LESSON = "lesson"
    }

    private data class Exercise(
        val question: String,
        val options: List<String>,
        val correct: Int,
        val explanation: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "🐍  Python"
        val index = intent.getIntExtra(EXTRA_LESSON, 0)
        val exercises = pythonExercises()
        val exercise = exercises[index.coerceIn(0, exercises.lastIndex)]

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val header = TextView(this).apply {
            text = "🧪 Prática ${index + 1} de ${exercises.size} • $language"
            textSize = 20f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val question = TextView(this).apply {
            text = exercise.question
            textSize = 21f
            setTextColor(Color.WHITE)
            setPadding(0, 26, 0, 22)
        }

        val alreadyCompleted = ProgressManager.isExerciseCompleted(this, language, index)\n\n        val feedback = TextView(this).apply {
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setPadding(0, 20, 0, 12)
        }

        screen.addView(header)
        screen.addView(question)

        exercise.options.forEachIndexed { optionIndex, option ->
            val button = Button(this).apply {
                text = option
                textSize = 16f
                isAllCaps = false
            }

            button.setOnClickListener {
                if (optionIndex == exercise.correct) {
                    val firstTime = ProgressManager.completeExercise(this, language, index)
                    ProgressManager.completeLesson(this, language, index)
                    button.text = "✓ $option"
                    button.isEnabled = false
                    feedback.text = if (firstTime) {
                        "🎉 Correto!\n\n${exercise.explanation}\n\n+25 XP"
                    } else {
                        "🎉 Correto!\n\n${exercise.explanation}\n\nExercício já concluído."
                    }

                    screen.findViewsByType().filter { it !== button }.forEach {
                        it.isEnabled = false
                    }
                } else {
                    feedback.text = "❌ Ainda não.\n\nLeia a explicação da aula novamente e tente outra alternativa."
                }
            }

            screen.addView(button, LinearLayout.LayoutParams(-1, 65).apply {
                setMargins(0, 6, 0, 6)
            })
        }

        val hint = TextView(this).apply {
            text = "💡 Dica: não tente decorar. Pense no que o código precisa fazer."
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 18, 0, 10)
        }
        screen.addView(hint)
        screen.addView(feedback)

        val next = Button(this).apply {
            isEnabled = alreadyCompleted\n            text = if (index < exercises.lastIndex) "Próxima prática →" else "Voltar às aulas práticas"
            isAllCaps = false
            setOnClickListener {
                if (index < exercises.lastIndex) {
                    startActivity(Intent(this@ExerciseActivity, ExerciseActivity::class.java).apply {
                        putExtra(EXTRA_LANGUAGE, language)
                        putExtra(EXTRA_LEVEL, intent.getStringExtra(EXTRA_LEVEL) ?: "Prática")
                        putExtra(EXTRA_LESSON, index + 1)
                    })
                    finish()
                } else {
                    finish()
                }
            }
        }
        screen.addView(next, LinearLayout.LayoutParams(-1, 60).apply {
            setMargins(0, 10, 0, 6)
        })

        val back = Button(this).apply {
            text = "← Voltar às aulas práticas"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))

        setContentView(screen)
    }

    private fun pythonExercises(): List<Exercise> = listOf(
        Exercise("Qual código mostra uma mensagem na tela?",
            listOf("print(\"Olá!\")", "show(\"Olá!\")", "display = \"Olá!\""), 0,
            "Em Python, print() é usado para mostrar uma mensagem."),
        Exercise("Qual linha cria uma variável chamada nome com o texto Ana?",
            listOf("nome = \"Ana\"", "nome == \"Ana\"", "var nome: Ana"), 0,
            "O sinal = atribui um valor à variável."),
        Exercise("Qual é o tipo do valor 20 em Python?",
            listOf("int", "str", "bool"), 0,
            "20 é um número inteiro, representado pelo tipo int."),
        Exercise("Qual código imprime Maior somente quando idade é 18 ou mais?",
            listOf("if idade >= 18:\\n    print(\"Maior\")", "if idade = 18:\\n    print(\"Maior\")", "for idade >= 18:\\n    print(\"Maior\")"), 0,
            "if verifica uma condição antes de executar o bloco."),
        Exercise("Qual código repete print(numero) cinco vezes com números de 0 a 4?",
            listOf("for numero in range(5):\\n    print(numero)", "repeat 5 print(numero)", "for numero = 5:\\n    print(numero)"), 0,
            "range(5) produz os valores 0, 1, 2, 3 e 4."),
        Exercise("Qual código define uma função chamada saudacao?",
            listOf("def saudacao():\\n    print(\"Olá\")", "function saudacao() { }", "create saudacao()"), 0,
            "Em Python, uma função começa com def."),
        Exercise("Qual valor é uma lista em Python?",
            listOf("[\"maçã\", \"banana\"]", "(\"maçã\", \"banana\")", "{\"maçã\": \"banana\"}"), 0,
            "Listas em Python usam colchetes."),
        Exercise("Qual linha pede um valor ao usuário e guarda o resultado em nome?",
            listOf("nome = input(\"Seu nome: \")", "input.nome = \"Seu nome\"", "read nome from user"), 0,
            "input() mostra uma pergunta e retorna o texto digitado.")
    )

    private fun LinearLayout.findViewsByType(): List<Button> {
        val result = mutableListOf<Button>()
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is Button) result.add(child)
        }
        return result
    }
}
