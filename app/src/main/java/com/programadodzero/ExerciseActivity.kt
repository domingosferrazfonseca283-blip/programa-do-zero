package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class ExerciseActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_LESSON = "lesson"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "🐍  Python"
        val level = intent.getStringExtra(EXTRA_LEVEL) ?: "Prática"
        val lesson = intent.getIntExtra(EXTRA_LESSON, 0)

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = "🧩 Prática de código"
            textSize = 24f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val info = TextView(this).apply {
            text = "A prática agora usa um editor de código real."
            textSize = 17f
            setTextColor(Color.LTGRAY)
            setPadding(0, 18, 0, 24)
        }

        val open = Button(this).apply {
            text = "⌨️ Abrir editor"
            isAllCaps = false
            setOnClickListener {
                startActivity(Intent(this@ExerciseActivity, PracticeCodingActivity::class.java).apply {
                    putExtra(PracticeCodingActivity.EXTRA_LANGUAGE, language)
                    putExtra(PracticeCodingActivity.EXTRA_LEVEL, level)
                    putExtra(PracticeCodingActivity.EXTRA_LESSON, lesson)
                })
                finish()
            }
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(title)
        screen.addView(info)
        screen.addView(open, LinearLayout.LayoutParams(-1, 65))
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))
        setContentView(screen)
    }
}
