package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class LessonActivity : Activity() {

    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linguagem = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        val nivel = intent.getStringExtra(EXTRA_LEVEL) ?: "Nível"

        val tela = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 50, 40, 40)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val topo = TextView(this).apply {
            text = "📚 $linguagem • $nivel"
            textSize = 22f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val progresso = TextView(this).apply {
            text = "Aula 1 de 5  •  0% concluído"
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 16, 0, 28)
        }

        val titulo = TextView(this).apply {
            text = "Aula 1 — O que é programação?"
            textSize = 28f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val texto = TextView(this).apply {
            text = """
                Programar é escrever instruções para que um computador realize tarefas.

                Nesta trilha, você vai aprender passo a passo, começando pelos conceitos fundamentais e avançando até projetos reais.

                Seu primeiro objetivo é entender três ideias:

                • dados
                • instruções
                • decisões

                Depois, vamos transformar esses conceitos em código usando $linguagem.
            """.trimIndent()
            textSize = 18f
            setTextColor(Color.LTGRAY)
            setPadding(0, 24, 0, 20)
        }

        val continuar = Button(this).apply {
            text = "Começar aula →"
            textSize = 17f
            isAllCaps = false
        }

        continuar.setOnClickListener {
            progresso.text = "Aula 1 de 5  •  20% concluído"
            continuar.text = "Aula concluída ✓"
            continuar.isEnabled = false
        }

        val voltar = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        tela.addView(topo)
        tela.addView(progresso)
        tela.addView(titulo)
        tela.addView(texto, LinearLayout.LayoutParams(-1, 0, 1f))
        tela.addView(continuar, LinearLayout.LayoutParams(-1, 65))
        tela.addView(voltar, LinearLayout.LayoutParams(-1, 60))

        setContentView(tela)
    }
}
