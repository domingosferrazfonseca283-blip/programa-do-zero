package com.programadodzero

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView

class CodeEditorActivity : Activity() {

    private lateinit var editor: EditText
    private lateinit var output: TextView
    private val inputs = mutableListOf<String>()
    private var earnedXpForCurrentRun = false
    private var projectMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra("language") ?: "🐍  Python"
        projectMode = intent.getBooleanExtra("project_mode", false)

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 24)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = if (projectMode) "🎮 Executar meu jogo" else "⌨️ Seu primeiro código"
            textSize = 25f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val info = TextView(this).apply {
            text = if (projectMode) {
                "Linguagem: $language\nExecute seu jogo e teste o palpite."
            } else {
                "Linguagem: $language\nEscreva o código e execute o exercício."
            }
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setPadding(0, 14, 0, 18)
        }

        editor = EditText(this).apply {
            setText(
                "numero_secreto = 7\n" +
                    "palpite = int(input(\"Digite seu palpite: \"))\n\n" +
                    "if palpite == numero_secreto:\n" +
                    "    print(\"Acertou!\")\n" +
                    "elif palpite > numero_secreto:\n" +
                    "    print(\"Muito alto!\")\n" +
                    "else:\n" +
                    "    print(\"Muito baixo!\")"
            )
            textSize = 17f
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            setBackgroundColor(Color.rgb(30, 41, 59))
            typeface = Typeface.MONOSPACE
            gravity = android.view.Gravity.TOP or android.view.Gravity.START
            setPadding(18, 18, 18, 18)
            minLines = 8
        }

        output = TextView(this).apply {
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
            inputs.clear()
            earnedXpForCurrentRun = false
            executePython(language)
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

    private fun executePython(language: String) {
        val code = editor.text.toString().trim()
        if (code.isEmpty()) {
            output.text = "⚠️ Escreva algum código primeiro."
            return
        }

        if (language != "🐍  Python") {
            output.text = "ℹ️ O executor Python está disponível nesta versão.\n\nOs executores das outras linguagens serão adicionados depois."
            return
        }

        val result = PythonRunner.run(code, inputs)

        if (result.needsInput) {
            askForInput(result.inputPrompt)
            return
        }

        output.text = if (result.success) {
            "▶ Resultado da execução\n\n" + result.output
        } else {
            "❌ Erro ao executar\n\n" + result.output
        }

        if (result.success && !earnedXpForCurrentRun) {
            ProgressManager.addXp(this, 10)
            earnedXpForCurrentRun = true
            if (projectMode) {
                ProgressManager.completeProject(this, 1)
                output.append("\n\n🏆 Execução concluída! +10 XP")
            }
        }
    }

    private fun askForInput(prompt: String) {
        val input = EditText(this).apply {
            hint = "Digite sua resposta"
            textSize = 17f
            setSingleLine(true)
        }

        AlertDialog.Builder(this)
            .setTitle("⌨️ Seu programa pediu uma entrada")
            .setMessage(if (prompt.isBlank()) "Digite um valor:" else prompt)
            .setView(input)
            .setPositiveButton("Enviar") { _, _ ->
                inputs.add(input.text.toString())
                executePython("🐍  Python")
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}
