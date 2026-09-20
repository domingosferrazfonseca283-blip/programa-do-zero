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

class LevelActivity : Activity() {

    companion object {
        const val EXTRA_LANGUAGE = "language"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linguagem = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"

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
            text = "Escolha seu ponto de partida."
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
            }

            val parametros = LinearLayout.LayoutParams(-1, 70).apply {
                setMargins(0, 8, 0, 8)
            }

            tela.addView(botao, parametros)

            botao.setOnClickListener {
                val intent = Intent(this, LessonActivity::class.java)
                intent.putExtra(LessonActivity.EXTRA_LANGUAGE, linguagem)
                intent.putExtra(LessonActivity.EXTRA_LEVEL, nivel)
                startActivity(intent)
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
