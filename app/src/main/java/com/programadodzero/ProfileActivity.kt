package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ProfileActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra("language") ?: "🐍  Python"
        val totalLessons = 8
        val xp = ProgressManager.getXp(this)
        val completed = ProgressManager.completedCount(this, language, totalLessons)
        val percent = completed * 100 / totalLessons

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(36, 50, 36, 36)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = "🏆 Meu progresso"
            textSize = 28f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = language
            textSize = 19f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 30)
        }

        val stats = TextView(this).apply {
            text = """
                ⭐ XP: $xp
                📚 Aulas concluídas: $completed/$totalLessons
                🧩 Exercícios concluídos: $completed/$totalLessons
                📈 Progresso: $percent%
            """.trimIndent()
            textSize = 20f
            setTextColor(Color.WHITE)
            setPadding(0, 20, 0, 30)
        }

        val message = TextView(this).apply {
            text = when {
                percent == 0 -> "🌱 Comece sua primeira aula. Cada passo conta!"
                percent < 50 -> "🔥 Você começou! Continue construindo sua base."
                percent < 100 -> "🚀 Muito bem! Você já está avançando bastante."
                else -> "🏆 Trilha concluída! Agora é hora de construir um projeto."
            }
            textSize = 18f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 10, 0, 30)
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(title, LinearLayout.LayoutParams(-1, -2))
        screen.addView(subtitle, LinearLayout.LayoutParams(-1, -2))
        screen.addView(stats, LinearLayout.LayoutParams(-1, -2))
        screen.addView(message, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))

        setContentView(screen)
    }
}
