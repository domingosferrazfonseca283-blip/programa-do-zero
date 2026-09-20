package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
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
            text = "Escolha um módulo para continuar sua formação."
            textSize = 17f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 24)
        }
        screen.addView(intro)

        for (module in modules) {
            val lessons = ContentRepository.lessonsForModule(language, level, module.order)
            val button = Button(this).apply {
                text = "Módulo " + module.order + " — " + module.title + "
" + module.description + "
" + lessons.size + " aula(s)"
                textSize = 16f
                isAllCaps = false
                setOnClickListener {
                    startActivity(Intent(this@ModuleActivity, LessonActivity::class.java).apply {
                        putExtra(LessonActivity.EXTRA_LANGUAGE, language)
                        putExtra(LessonActivity.EXTRA_LEVEL_NUMBER, level)
                        putExtra(LessonActivity.EXTRA_MODULE, module.order)
                    })
                }
            }
            screen.addView(button, LinearLayout.LayoutParams(-1, 88).apply { setMargins(0, 7, 0, 7) })
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }
        screen.addView(back, LinearLayout.LayoutParams(-1, -2).apply { setMargins(0, 20, 0, 0) })
        setContentView(screen)
    }
}