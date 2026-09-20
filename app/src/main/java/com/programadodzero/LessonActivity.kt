package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class LessonActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_LESSON = "lesson"
    }

    private lateinit var progress: TextView
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var code: TextView
    private lateinit var nextButton: Button
    private lateinit var lessons: List<LessonContent>
    private lateinit var language: String
    private lateinit var level: String
    private var currentLesson = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        level = intent.getStringExtra(EXTRA_LEVEL) ?: "Nível"
        lessons = ContentRepository.lessonsFor(language)
        currentLesson = intent.getIntExtra(EXTRA_LESSON, firstIncompleteLesson())

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 40, 32, 32)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        val header = TextView(this).apply {
            text = "📚 $language • $level\n${moduleSummary()}"
            textSize = 21f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }
        progress = TextView(this).apply { textSize = 16f; setTextColor(Color.LTGRAY); setPadding(0,14,0,22) }
        title = TextView(this).apply { textSize = 27f; setTextColor(Color.WHITE); setTypeface(null, Typeface.BOLD) }
        body = TextView(this).apply { textSize = 18f; setTextColor(Color.LTGRAY); setPadding(0,18,0,18) }
        code = TextView(this).apply { textSize = 16f; setTextColor(Color.WHITE); setPadding(20,18,20,18); setBackgroundColor(Color.rgb(30,41,59)); typeface = Typeface.MONOSPACE }
        nextButton = Button(this).apply { textSize = 17f; isAllCaps = false }

        nextButton.setOnClickListener {
            if (!ProgressManager.isLessonCompleted(this, language, currentLesson)) {
                startExercise()
            } else if (currentLesson < lessons.lastIndex) {
                currentLesson++
                showLesson()
            }
        }

        val backButton = Button(this).apply { text = "← Voltar"; isAllCaps = false; setOnClickListener { finish() } }
        screen.addView(header)
        screen.addView(progress)
        screen.addView(title)
        screen.addView(body, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(code, LinearLayout.LayoutParams(-1, -2))
        screen.addView(nextButton, LinearLayout.LayoutParams(-1, 65))
        screen.addView(backButton, LinearLayout.LayoutParams(-1, 60))
        setContentView(screen)
        showLesson()
    }

    override fun onResume() { super.onResume(); if (::lessons.isInitialized) showLesson() }

    private fun showLesson() {
        currentLesson = currentLesson.coerceIn(0, lessons.lastIndex)
        val lesson = lessons[currentLesson]
        val completed = ProgressManager.isLessonCompleted(this, language, currentLesson)
        val number = currentLesson + 1
        progress.text = "Aula $number de ${lessons.size} • ${((number - 1) * 100 / lessons.size)}% estudado"
        title.text = lesson.title
        body.text = lesson.body
        code.text = "Módulo ${lesson.module} • Exemplo:\n\n${lesson.code}"
        nextButton.text = when {
            !completed -> "🧩 Fazer prática da aula"
            currentLesson < lessons.lastIndex -> "✅ Prática concluída • Próxima aula →"
            else -> "🏆 Trilha concluída"
        }
        nextButton.isEnabled = !(completed && currentLesson == lessons.lastIndex)
    }

    private fun startExercise() {
        startActivity(Intent(this, PracticeCodingActivity::class.java).apply {
            putExtra(PracticeCodingActivity.EXTRA_LANGUAGE, language)
            putExtra(PracticeCodingActivity.EXTRA_LEVEL, level)
            putExtra(PracticeCodingActivity.EXTRA_LESSON, currentLesson)
        })
    }

    private fun moduleSummary(): String {
        if (lessons.isEmpty()) return "Conteúdo em construção"
        val module = lessons[currentLesson.coerceIn(0, lessons.lastIndex)].module
        val levelNumber = lessons[currentLesson.coerceIn(0, lessons.lastIndex)].level
        val info = ContentRepository.modulesFor(language).firstOrNull { it.level == levelNumber && it.order == module }
        return if (info != null) "Módulo $module — ${info.title}" else "Módulo $module"
    }

    private fun firstIncompleteLesson(): Int =
        (0 until lessons.size).firstOrNull { !ProgressManager.isLessonCompleted(this, language, it) } ?: lessons.lastIndex

}