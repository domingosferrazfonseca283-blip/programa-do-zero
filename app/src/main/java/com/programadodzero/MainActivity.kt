package com.programadodzero

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val tela = LinearLayout(this)

        tela.orientation = LinearLayout.VERTICAL
        tela.gravity = Gravity.CENTER_HORIZONTAL
        tela.setPadding(40, 60, 40, 40)
        tela.setBackgroundColor(Color.rgb(15, 23, 42))

        val titulo = TextView(this)

        titulo.text = "👨‍💻 Bem-vindo!"
        titulo.textSize = 30f
        titulo.setTextColor(Color.WHITE)
        titulo.setTypeface(null, Typeface.BOLD)
        titulo.gravity = Gravity.CENTER

        tela.addView(
            titulo,
            LinearLayout.LayoutParams(
                -1,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

        val subtitulo = TextView(this)

        subtitulo.text = "\nEscolha uma linguagem para começar sua jornada:"
        subtitulo.textSize = 18f
        subtitulo.setTextColor(Color.LTGRAY)
        subtitulo.gravity = Gravity.CENTER

        tela.addView(
            subtitulo,
            LinearLayout.LayoutParams(
                -1,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )

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

            val botao = Button(this)

            botao.text = linguagem
            botao.textSize = 17f

            val parametros = LinearLayout.LayoutParams(
                -1,
                65
            )

            parametros.setMargins(0, 8, 0, 8)

            tela.addView(botao, parametros)

            botao.setOnClickListener {
                Toast.makeText(
                    this,
                    "Você escolheu $linguagem",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setContentView(tela)
    }
}
