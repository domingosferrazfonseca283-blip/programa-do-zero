package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class PracticalLessonsActivity : Activity() {

    private val lessonTitles = listOf(
        "1. O que é programação?",
        "2. Variáveis",
        "3. Tipos de dados",
        "4. Condições",
        "5. Repetições",
        "6. Funções",
        "7. Listas",
        "8. Primeiro projeto"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra("language") ?: "🐍  Python"

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = "🧪 Aulas práticas"
            textSize = 27f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val subtitle = TextView(this).apply {
            text = "$language • aprenda fazendo"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            setPadding(0, 10, 0, 20)
        }

        screen.addView(title)
        screen.addView(subtitle)

        lessonTitles.forEachIndexed { index, lessonTitle ->
            val completed = ProgressManager.isExerciseCompleted(this, language, index)

            val button = Button(this).apply {
                text = if (completed) "✅ $lessonTitle  •  concluída" else "🧩 $lessonTitle"
                textSize = 16f
                isAllCaps = false
                setOnClickListener {
                    startActivity(Intent(this@PracticalLessonsActivity, ExerciseActivity::class.java).apply {
                        putExtra(ExerciseActivity.EXTRA_LANGUAGE, language)
                        putExtra(ExerciseActivity.EXTRA_LEVEL, "Prática")
                        putExtra(ExerciseActivity.EXTRA_LESSON, index)
                    })
                }
            }

            screen.addView(button, LinearLayout.LayoutParams(-1, 64).apply {
                setMargins(0, 5, 0, 5)
            })
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        screen.addView(back, LinearLayout.LayoutParams(-1, 60).apply {
            setMargins(0, 14, 0, 0)
        })

        setContentView(screen)
    }

    override fun onResume() {
        super.onResume()
        recreate()
    }
}
