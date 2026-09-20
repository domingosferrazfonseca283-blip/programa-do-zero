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
    private lateinit var project: ProjectContent
    private lateinit var language: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = intent.getStringExtra("language") ?: "🐍  Python"
        project = ContentRepository.projectFor(language) ?: run { finish(); return }
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
            text = "🎮 " + project.title
            textSize = 22f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }
        val help = TextView(this).apply {
            text = project.description
            textSize = 16f
            setTextColor(Color.LTGRAY)
            setPadding(0, 12, 0, 12)
        }
        editor = EditText(this).apply {
            setText(project.starter)
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
            setOnClickListener { step++; showStep() }
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
        if (step >= project.steps.size) {
            feedback.text = "🏆 Todas as etapas foram concluídas.\n\nExecute o projeto para validar o programa completo."
            play.isEnabled = true
            next.isEnabled = false
            return
        }
        play.isEnabled = false
        val item = project.steps[step]
        feedback.text = "🎯 ${item.title}\n\n${item.instruction}\n\n💡 ${item.hint}"
        next.isEnabled = false
    }

    private fun verifyStep() {
        val code = editor.text.toString()
        val ok = when (step) {
            0 -> code.contains("numero_secreto") && code.contains("=")
            1 -> code.contains("input(")
            2 -> code.contains("if ") && code.contains("==")
            3 -> (code.contains("elif ") || code.contains("else:")) && code.contains("print(")
            4 -> code.contains("while ") && code.contains("tentativas") && (code.contains("+ 1") || code.contains("+1"))
            else -> false
        }
        if (!ok) {
            val item = project.steps[step]
            feedback.text = "❌ Ainda falta uma parte desta etapa.\n\n💡 ${item.hint}\n\nExemplo:\n${item.example}"
            return
        }
        if (step == project.steps.lastIndex) {
            feedback.text = "✅ Etapa concluída!\n\nAgora execute o jogo para testar o código completo."
            next.isEnabled = false
            play.isEnabled = true
        } else {
            feedback.text = "✅ Etapa concluída!\n\nVocê entendeu esta parte. Agora avance para a próxima."
            next.isEnabled = true
        }
    }

    private fun openPlayableGame() {
        startActivity(android.content.Intent(this, CodeEditorActivity::class.java).apply {
            putExtra("language", language)
            putExtra("project_mode", true)
        })
    }
}
