package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class ExerciseActivity : Activity() {

    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_LESSON = "lesson"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        val lesson = intent.getIntExtra(EXTRA_LESSON, 0)

        val questions = listOf(
            "Qual é o principal objetivo de um programa de computador?",
            "O que uma variável pode armazenar?",
            "Por que é importante conhecer os tipos de dados?",
            "Qual estrutura normalmente usamos para tomar uma decisão?",
            "Para que servem os laços de repetição?"
        )

        val answers = listOf(
            listOf("Executar instruções para realizar tarefas", "Apenas desligar o computador", "Somente mostrar imagens"),
            listOf("Um valor que o programa pode usar", "Somente arquivos", "Apenas o teclado"),
            listOf("Para trabalhar corretamente com diferentes valores", "Para deixar o código maior", "Para evitar variáveis"),
            listOf("if", "print", "import"),
            listOf("Repetir tarefas sem duplicar código", "Apagar o programa", "Criar uma senha")
        )

        val index = lesson.coerceIn(0, questions.lastIndex)

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 40, 32, 32)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val header = TextView(this).apply {
            text = "🧩 Exercício • $language"
            textSize = 22f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val question = TextView(this).apply {
            text = questions[index]
            textSize = 21f
            setTextColor(Color.WHITE)
            setPadding(0, 28, 0, 24)
        }

        screen.addView(header)
        screen.addView(question)

        answers[index].forEachIndexed { answerIndex, answer ->
            val button = Button(this).apply {
                text = answer
                textSize = 16f
                isAllCaps = false
            }

            button.setOnClickListener {
                if (answerIndex == 0) {
                    Toast.makeText(this, "Resposta correta! ✓", Toast.LENGTH_SHORT).show()
                    button.text = "✓ $answer"
                    button.isEnabled = false
                } else {
                    Toast.makeText(this, "Ainda não. Tente novamente!", Toast.LENGTH_SHORT).show()
                }
            }

            screen.addView(button, LinearLayout.LayoutParams(-1, 65).apply {
                setMargins(0, 8, 0, 8)
            })
        }

        val hint = TextView(this).apply {
            text = "💡 Dica: pense no conceito explicado na aula."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 16)
        }
        screen.addView(hint)

        val back = Button(this).apply {
            text = "← Voltar para a aula"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))

        setContentView(screen)
    }
}
