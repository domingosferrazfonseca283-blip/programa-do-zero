package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class LevelActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        private const val TOTAL_LESSONS = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linguagem = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        val active = ProgressManager.getActiveLanguage(this)

        val tela = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(40, 60, 40, 40)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val titulo = TextView(this).apply {
            text = "🎯 Seu nível em $linguagem"
            textSize = 26f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val subtitulo = TextView(this).apply {
            text = if (active != null && active != linguagem) {
                "🔒 Você está estudando $active.\nConclua essa trilha para desbloquear outra linguagem."
            } else {
                "Escolha seu ponto de partida. Você poderá aprender outra linguagem depois de concluir esta trilha."
            }
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 30)
        }

        tela.addView(titulo, LinearLayout.LayoutParams(-1, -2))
        tela.addView(subtitulo, LinearLayout.LayoutParams(-1, -2))

        val niveis = listOf(
            "🌱  Nunca programei",
            "📘  Básico",
            "🧠  Intermediário",
            "🔥  Avançado"
        )

        for (nivel in niveis) {
            val botao = Button(this).apply {
                text = nivel
                textSize = 17f
                isAllCaps = false
                isEnabled = ProgressManager.canStartLanguage(
                    this@LevelActivity, linguagem, TOTAL_LESSONS
                )
            }

            tela.addView(botao, LinearLayout.LayoutParams(-1, 70).apply {
                setMargins(0, 8, 0, 8)
            })

            botao.setOnClickListener {
                if (ProgressManager.selectLanguage(this, linguagem, TOTAL_LESSONS)) {
                    startActivity(android.content.Intent(this, LessonActivity::class.java).apply {
                        putExtra(LessonActivity.EXTRA_LANGUAGE, linguagem)
                        putExtra(LessonActivity.EXTRA_LEVEL, nivel)
                    })
                } else {
                    Toast.makeText(
                        this,
                        "Conclua $active antes de começar outra linguagem.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        val voltar = Button(this).apply {
            text = "← Voltar"
            textSize = 16f
            isAllCaps = false
            setOnClickListener { finish() }
        }

        tela.addView(voltar, LinearLayout.LayoutParams(-1, 60).apply {
            setMargins(0, 24, 0, 0)
        })

        setContentView(tela)
    }
}
