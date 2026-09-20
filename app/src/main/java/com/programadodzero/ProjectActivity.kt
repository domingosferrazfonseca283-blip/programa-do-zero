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
    private var step = 0
    private lateinit var editor: EditText
    private lateinit var feedback: TextView
    private lateinit var next: Button
    private lateinit var play: Button

    private val steps = listOf(
        "1/5 — Crie o número secreto" to "Crie uma variável chamada numero_secreto com um número.",
        "2/5 — Peça o palpite" to "Use input() para pedir um palpite e guarde a resposta em uma variável.",
        "3/5 — Compare os números" to "Use if para verificar se o palpite é igual ao número secreto.",
        "4/5 — Dê uma dica" to "Use elif ou else para informar se o palpite é maior ou menor.",
        "5/5 — Conte tentativas" to "Crie tentativas e aumente esse contador quando o jogador tentar."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildScreen()
        showStep()
    }

    private fun buildScreen() {
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

        val help = TextView(this).apply {
            text = "Construa o jogo por etapas. Você pode corrigir o código e tentar novamente."
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 12, 0, 12)
        }

        editor = EditText(this).apply {
            setText("numero_secreto = 7")
            textSize = 16f
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(30, 41, 59))
            typeface = Typeface.MONOSPACE
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            setPadding(14, 14, 14, 14)
            minLines = 10
        }

        feedback = TextView(this).apply {
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 12, 0, 8)
        }

        val check = Button(this).apply {
            text = "▶ Verificar etapa"
            isAllCaps = false
            setOnClickListener { verifyStep() }
        }

        next = Button(this).apply {
            text = "Próxima etapa →"
            isAllCaps = false
            isEnabled = false
            setOnClickListener {
                step++
                showStep()
            }
        }

        play = Button(this).apply {
            text = "🎮 Executar meu jogo"
            isAllCaps = false
            isEnabled = false
            setOnClickListener { openPlayableGame() }
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(title)
        screen.addView(help)
        screen.addView(editor, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(feedback)
        screen.addView(check, LinearLayout.LayoutParams(-1, 60))
        screen.addView(next, LinearLayout.LayoutParams(-1, 60))
        screen.addView(play, LinearLayout.LayoutParams(-1, 60))
        screen.addView(back, LinearLayout.LayoutParams(-1, 58))
        setContentView(screen)
    }

    private fun showStep() {
        if (step >= steps.size) {
            play.isEnabled = true
            return
        }
        play.isEnabled = false
        val item = steps[step]
        feedback.text = "🎯 " + item.first + "\n\n" + item.second + "\n\n💡 Mantenha o código anterior e acrescente a nova parte."
        next.isEnabled = false
    }

    private fun verifyStep() {
        val code = editor.text.toString()
        val ok = when (step) {
            0 -> code.contains("numero_secreto") && code.contains("=")
            1 -> code.contains("input(")
            2 -> code.contains("if ") && code.contains("==")
            3 -> (code.contains("elif ") || code.contains("else:")) && code.contains("print(")
            4 -> code.contains("tentativas") && (code.contains("+ 1") || code.contains("+1"))
            else -> false
        }

        if (!ok) {
            feedback.text = when (step) {
                0 -> "❌ Falta a variável numero_secreto.\n\n💡 Exemplo: numero_secreto = 7"
                1 -> "❌ Falta input().\n\n💡 Use palpite = int(input(\"Digite seu palpite: \"))"
                2 -> "❌ Falta comparar os valores.\n\n💡 Use if palpite == numero_secreto:"
                3 -> "❌ Falta uma alternativa com elif ou else e uma mensagem.\n\n💡 Use print() para explicar a dica."
                4 -> "❌ Falta controlar as tentativas.\n\n💡 Crie tentativas = 0 e depois aumente com tentativas = tentativas + 1."
                else -> "❌ Revise esta etapa."
            }
            return
        }

        if (step == steps.lastIndex) {
            feedback.text = "✅ Etapa concluída!\n\nAgora execute o jogo para testar seu código de verdade.\n\n🏆 O projeto será concluído quando a execução funcionar."
            next.isEnabled = false
            play.isEnabled = true
        } else {
            feedback.text = "✅ Etapa concluída!\n\nVocê entendeu esta parte. Agora avance para a próxima."
            next.isEnabled = true
        }
    }

    private fun openPlayableGame() {
        val intent = android.content.Intent(this, CodeEditorActivity::class.java).apply {
            putExtra("language", "🐍  Python")
            putExtra("project_mode", true)
        }
        startActivity(intent)
    }
}
