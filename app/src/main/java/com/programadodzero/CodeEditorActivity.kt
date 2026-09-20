package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class CodeEditorActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra("language") ?: "🐍  Python"

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = "⌨️ Seu primeiro código"
            textSize = 25f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val info = TextView(this).apply {
            text = "Linguagem: $language\nEscreva o código e execute o exercício."
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setPadding(0, 14, 0, 18)
        }

        val editor = EditText(this).apply {
            setText("print(\"Olá, mundo!\")")
            textSize = 17f
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setBackgroundColor(Color.rgb(30, 41, 59))
            typeface = Typeface.MONOSPACE
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            setPadding(18, 18, 18, 18)
            minLines = 8
        }

        val output = TextView(this).apply {
            text = "Saída aparecerá aqui..."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(18, 18, 18, 18)
        }

        val run = Button(this).apply {
            text = "▶ Executar"
            textSize = 17f
            isAllCaps = false
        }

        run.setOnClickListener {
            val code = editor.text.toString().trim()
            if (code.isEmpty()) {
                output.text = "⚠️ Escreva algum código primeiro."
                return@setOnClickListener
            }

            if (language == "🐍  Python" && code.contains("print(")) {
                val text = extractPrint(code)
                output.text = "✅ Execução simulada\n\n$text"
                ProgressManager.addXp(this, 10)
            } else {
                output.text = "ℹ️ Este editor está em modo de prática.\n\nO executor completo da linguagem será adicionado nas próximas versões."
            }
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(title)
        screen.addView(info)
        screen.addView(editor, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(run, LinearLayout.LayoutParams(-1, 65))
        screen.addView(output, LinearLayout.LayoutParams(-1, -2))
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))

        setContentView(screen)
    }

    private fun extractPrint(code: String): String {
        val start = code.indexOf("print(") + 6
        val end = code.indexOf(")", start)
        if (start <= 5 || end < start) return "Código recebido."
        return code.substring(start, end).trim().removeSurrounding("\"", "\"")
    }
}
