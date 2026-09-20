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

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tela = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(40, 60, 40, 40)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val titulo = TextView(this).apply {
            text = "👨‍💻 Programação do Zero"
            textSize = 28f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val subtitulo = TextView(this).apply {
            text = "Escolha uma linguagem para começar sua jornada"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 24)
        }

        tela.addView(titulo, LinearLayout.LayoutParams(-1, -2))
        tela.addView(subtitulo, LinearLayout.LayoutParams(-1, -2))

        val linguagens = listOf(
            "🐍  Python",
            "🌐  JavaScript",
            "🔷  TypeScript",
            "☕  Java",
            "⚙️  C",
            "🚀  C++",
            "🦀  Rust",
            "🐹  Go"
        )

        for (linguagem in linguagens) {
            val botao = Button(this).apply {
                text = linguagem
                textSize = 17f
                isAllCaps = false
            }

            val parametros = LinearLayout.LayoutParams(-1, 65).apply {
                setMargins(0, 6, 0, 6)
            }

            tela.addView(botao, parametros)

            botao.setOnClickListener {
                val intent = Intent(this, LevelActivity::class.java)
                intent.putExtra(LevelActivity.EXTRA_LANGUAGE, linguagem)
                startActivity(intent)
            }
        }

        setContentView(tela)
    }
}
