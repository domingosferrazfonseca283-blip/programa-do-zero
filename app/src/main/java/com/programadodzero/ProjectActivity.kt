package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class ProjectActivity : Activity() {
    private lateinit var editor: EditText
    private lateinit var feedback: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra("language") ?: "🐍 Python"

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 28, 24, 24)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = "🎮 Projeto 1 — Jogo de Adivinhação"
            textSize = 22f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val goal = TextView(this).apply {
            text = "Você vai juntar o que aprendeu para criar um jogo.\n\nO programa deve:\n• escolher um número secreto\n• pedir um palpite\n• dizer se acertou\n• dizer se o palpite é maior ou menor\n• contar tentativas"
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setPadding(0, 14, 0, 14)
        }

        editor = EditText(this).apply {
            setText(
                "numero_secreto = 7\n" +
                "tentativas = 0\n\n" +
                "palpite = int(input(\"Digite seu palpite: \"))\n" +
                "tentativas = tentativas + 1\n\n" +
                "if palpite == numero_secreto:\n" +
                "    print(\"Acertou!\")\n" +
                "elif palpite < numero_secreto:\n" +
                "    print(\"Tente um número maior.\")\n" +
                "else:\n" +
                "    print(\"Tente um número menor.\")\n\n" +
                "print(\"Tentativas:\", tentativas)"
            )
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(30, 41, 59))
            typeface = Typeface.MONOSPACE
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            setPadding(14, 14, 14, 14)
            minLines = 12
        }

        feedback = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 12, 0, 8)
        }

        val check = Button(this).apply {
            text = "▶ Verificar projeto"
            isAllCaps = false
            setOnClickListener { verifyProject() }
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(title)
        screen.addView(goal)
        screen.addView(editor, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(feedback)
        screen.addView(check, LinearLayout.LayoutParams(-1, 60))
        screen.addView(back, LinearLayout.LayoutParams(-1, 58))
        setContentView(screen)
    }

    private fun verifyProject() {
        val code = editor.text.toString()
        val checks = listOf(
            "um número secreto" to (code.contains("numero_secreto") || code.contains("numeroSecreto")),
            "um contador de tentativas" to code.contains("tentativas"),
            "entrada com input()" to code.contains("input("),
            "conversão para número com int()" to code.contains("int("),
            "comparação com if" to code.contains("if "),
            "decisão alternativa com elif ou else" to (code.contains("elif ") || code.contains("else:")),
            "mensagem com print()" to code.contains("print(")
        )
        val missing = checks.filter { !it.second }.map { it.first }

        feedback.text = if (missing.isEmpty()) {
            ProgressManager.addXp(this, 100)
            "🏆 Projeto concluído!\n\nVocê juntou variáveis, entrada, números, condições e saída em um programa completo.\n\n+100 XP"
        } else {
            "🧩 O projeto ainda precisa de:\n\n" + missing.joinToString("\n") { "• $it" } +
                "\n\n💡 Corrija uma parte por vez e verifique novamente."
        }
    }
}
