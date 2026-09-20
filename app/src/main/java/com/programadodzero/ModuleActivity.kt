package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class ModuleActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        val level = intent.getIntExtra(EXTRA_LEVEL, 1)
        val modules = ContentRepository.modulesForLevel(language, level)
        val previousLevelLessons = ContentRepository.lessonsFor(language)
            .filter { it.level < level }
        val levelUnlocked = previousLevelLessons.all {
            ProgressManager.isLessonCompleted(this, language, it.id)
        }

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 40, 32, 32)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        val title = TextView(this).apply {
            text = "📚 $language • Nível $level"
            textSize = 26f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
            gravity = Gravity.CENTER
        }
        screen.addView(title, LinearLayout.LayoutParams(-1, -2))
        val intro = TextView(this).apply {
            text = if (levelUnlocked) {
                "Escolha um módulo para continuar sua formação."
            } else {
                "🔒 Este nível está bloqueado.\nConclua o nível anterior para liberar as aulas."
            }
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 24)
        }
        screen.addView(intro)

        for (module in modules) {
            val lessons = ContentRepository.lessonsForModule(language, level, module.order)
            val completed = lessons.count { ProgressManager.isLessonCompleted(this, language, it.id) }
            val previousModules = modules.filter { it.order < module.order }
            val previousLessons = previousModules.flatMap { previous ->
                ContentRepository.lessonsForModule(language, level, previous.order)
            }
            val unlocked = previousLessons.all {
                ProgressManager.isLessonCompleted(this, language, it.id)
            }
            val status = when {
                lessons.isEmpty() -> "🚧 Em construção"
                completed == lessons.size -> "✅ Concluído"
                unlocked -> "▶️ Disponível"
                else -> "🔒 Bloqueado"
            }
            val button = Button(this).apply {
                text = "Módulo " + module.order + " — " + module.title + "\n" +
                    module.description + "\n" + completed + "/" + lessons.size + " aulas • " + status
                textSize = 15f
                isAllCaps = false
                gravity = Gravity.CENTER
                includeFontPadding = false
                setPadding(16, 8, 16, 8)
                isEnabled = levelUnlocked && unlocked && lessons.isNotEmpty()
                alpha = if (isEnabled) 1f else 0.5f
                setOnClickListener {
                    val firstPending = lessons.firstOrNull {
                        !ProgressManager.isLessonCompleted(this@ModuleActivity, language, it.id)
                    } ?: lessons.lastOrNull()
                    if (firstPending != null) {
                        startActivity(Intent(this@ModuleActivity, LessonActivity::class.java).apply {
                            putExtra(LessonActivity.EXTRA_LANGUAGE, language)
                            putExtra(LessonActivity.EXTRA_LEVEL, "Nível " + level)
                            putExtra(LessonActivity.EXTRA_LEVEL_NUMBER, level)
                            putExtra(LessonActivity.EXTRA_MODULE, module.order)
                            putExtra(LessonActivity.EXTRA_LESSON_ID, firstPending.id)
                        })
                    }
                }
            }
            screen.addView(button, LinearLayout.LayoutParams(-1, 104).apply { setMargins(0, 7, 0, 7) })
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        screen.addView(back, LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 20, 0, 0) })
        val scroll = ScrollView(this).apply {
            isFillViewport = true
            addView(screen)
        }
        setContentView(scroll)
    }
}