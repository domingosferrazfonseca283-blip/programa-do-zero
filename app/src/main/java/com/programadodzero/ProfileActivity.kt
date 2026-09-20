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

        val language = intent.getStringExtra("language") ?: ProgressManager.getActiveLanguage(this) ?: "🐍  Python"
        val xp = ProgressManager.getXp(this)
        val level = ProgressManager.getLevel(this)
        val levelXp = ProgressManager.xpIntoLevel(this)
        val reviews = ProgressManager.completedReviewCount(this, language)
        val lessonIds = ContentRepository.lessonsFor(language).map { it.id }
        val total = lessonIds.size
        val lessons = ProgressManager.completedCount(this, language, lessonIds)
        val exercises = ProgressManager.completedExerciseCount(this, language, lessonIds)
        val percent = if (total == 0) 0 else (lessons + exercises) * 100 / (total * 2)
        val complete = total > 0 && ProgressManager.isLanguageComplete(this, language, lessonIds)

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
            text = if (complete) "🏆 $language • trilha concluída" else "🎯 $language • linguagem em foco"
            textSize = 19f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 30)
        }

        val stats = TextView(this).apply {
            text = "⭐ XP: $xp\n🔥 Nível do programador: $level\n📊 XP do nível: $levelXp/100\n📝 Revisões acertadas: $reviews\n📚 Aulas concluídas: $lessons/$total\n🧩 Exercícios concluídos: $exercises/$total\n📈 Progresso: $percent%"
            textSize = 20f
            setTextColor(Color.WHITE)
            setPadding(0, 20, 0, 30)
        }

        val message = TextView(this).apply {
            text = if (complete) "🎉 Você concluiu esta linguagem!\n\nAgora outra linguagem pode ser desbloqueada." else "🌱 Continue nesta linguagem até concluir a trilha completa.\n\nSó depois disso outra linguagem será liberada."
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

        screen.addView(title)
        screen.addView(subtitle)
        screen.addView(stats)
        screen.addView(message, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))
        setContentView(screen)
    }
}