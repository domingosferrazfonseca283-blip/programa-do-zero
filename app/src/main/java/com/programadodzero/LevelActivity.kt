package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.content.Intent
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class LevelActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val linguagem = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        val active = ProgressManager.getActiveLanguage(this)
        val available = ContentRepository.isLanguageAvailable(linguagem)
        val lessonIds = ContentRepository.lessonsFor(linguagem).map { it.id }

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
            text = when {
                !available -> "🚧 Esta trilha ainda está em construção.\nO conteúdo completo disponível hoje é Python.\nAs outras linguagens serão adicionadas por módulos, sem conteúdo genérico enganoso."
                active != null && active != linguagem -> "🔒 Você está estudando $active.\nConclua essa trilha para desbloquear outra linguagem."
                else -> "Escolha seu ponto de partida. Você poderá aprender outra linguagem depois de concluir esta trilha."
            }
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 30)
        }

        tela.addView(titulo, LinearLayout.LayoutParams(-1, -2))
        tela.addView(subtitulo, LinearLayout.LayoutParams(-1, -2))

        val nivelNomes = mapOf(
            1 to "🌱  Fundamentos",
            2 to "📘  Intermediário",
            3 to "🔥  Avançado"
        )
        val niveis = ContentRepository.modulesFor(linguagem)
            .map { it.level }
            .distinct()
            .sorted()

        for (nivelNumero in niveis) {
            val nivel = nivelNomes[nivelNumero] ?: "Nível $nivelNumero"
            val levelModules = ContentRepository.modulesForLevel(linguagem, nivelNumero)
            val levelLessons = ContentRepository.lessonsForLevel(linguagem, nivelNumero)
            val previousLessons = ContentRepository.lessonsFor(linguagem)
                .filter { it.level < nivelNumero }
            val previousComplete = previousLessons.all {
                ProgressManager.isLessonCompleted(this, linguagem, it.id)
            }
            val enabled = available &&
                ProgressManager.canStartLanguage(this, linguagem, lessonIds) &&
                previousComplete
            val completed = levelLessons.count {
                ProgressManager.isLessonCompleted(this, linguagem, it.id)
            }
            val status = when {
                completed == levelLessons.size && levelLessons.isNotEmpty() -> "✅ Concluído"
                enabled -> "▶️ Disponível"
                else -> "🔒 Conclua o nível anterior"
            }
            val botao = Button(this).apply {
                gravity = Gravity.CENTER
                includeFontPadding = false
                setPadding(16, 8, 16, 8)
                text = nivel + "\n" + completed + "/" + levelLessons.size + " aulas • " + status
                textSize = 16f
                isAllCaps = false
                isEnabled = enabled
                alpha = if (enabled) 1f else 0.5f
            }

            tela.addView(botao, LinearLayout.LayoutParams(-1, 70).apply {
                setMargins(0, 8, 0, 8)
            })

            botao.setOnClickListener {
                if (ProgressManager.selectLanguage(this, linguagem, lessonIds)) {
                    startActivity(Intent(this, ModuleActivity::class.java).apply {
                        putExtra(LessonActivity.EXTRA_LANGUAGE, linguagem)
                        putExtra(ModuleActivity.EXTRA_LEVEL, nivelNumero)
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
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
            text = "← Voltar"
            textSize = 16f
            isAllCaps = false
            setOnClickListener { finish() }
        }

        tela.addView(voltar, LinearLayout.LayoutParams(-1, -2).apply {
            setMargins(0, 24, 0, 0)
        })

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            addView(tela)
        }
        setContentView(scroll)
    }
}
