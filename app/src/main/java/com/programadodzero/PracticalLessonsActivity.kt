package com.programadodzero

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class PracticalLessonsActivity : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.rgb(15, 23, 42)
        window.navigationBarColor = Color.rgb(15, 23, 42)

        val language = intent.getStringExtra("language") ?: "🐍  Python"

        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(28, 36, 28, 28)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }

        val title = TextView(this).apply {
            text = "🧪 Aulas práticas (" + challenges.size + ")"
            textSize = 27f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        }

        val subtitle = TextView(this).apply {
            text = "$language • aprenda fazendo"
            textSize = 18f
            setTextColor(Color.LTGRAY)
            setPadding(0, 10, 0, 20)
        }

        screen.addView(title)
        screen.addView(subtitle)

        val challenges = ContentRepository.exercisesFor(language)
        challenges.forEachIndexed { index, challenge ->
            val completed = ProgressManager.isExerciseCompleted(this, language, challenge.id)
            val lessonTitle = challenge.title

            val button = Button(this).apply {
                text = if (completed) "✅ $lessonTitle  •  concluída" else "🧩 $lessonTitle"
                textSize = 16f
                isAllCaps = false
                setTextColor(Color.rgb(17, 24, 39))
                gravity = android.view.Gravity.CENTER
                includeFontPadding = false
                setPadding(16, 8, 16, 8)
                background = GradientDrawable().apply {
                    setColor(Color.rgb(229, 231, 235))
                    cornerRadius = 12f
                }
                setOnClickListener {
                    startActivity(Intent(this@PracticalLessonsActivity, PracticeCodingActivity::class.java).apply {
                        putExtra(PracticeCodingActivity.EXTRA_LANGUAGE, language)
                        putExtra(PracticeCodingActivity.EXTRA_LEVEL, "Prática")
                        putExtra(PracticeCodingActivity.EXTRA_LESSON_ID, challenge.id)
                        putExtra(PracticeCodingActivity.EXTRA_LESSON, index)
                    })
                }
            }

            screen.addView(button, LinearLayout.LayoutParams(-1, 64).apply {
                setMargins(0, 5, 0, 5)
            })
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            textSize = 16f
            setTextColor(Color.rgb(17, 24, 39))
            gravity = android.view.Gravity.CENTER
            includeFontPadding = false
            background = GradientDrawable().apply {
                setColor(Color.rgb(229, 231, 235))
                cornerRadius = 12f
            }
            setOnClickListener { finish() }
        }
        screen.addView(back, LinearLayout.LayoutParams(-1, 60).apply {
            setMargins(0, 14, 0, 0)
        })

        val scroll = ScrollView(this).apply {
            isFillViewport = true
            addView(screen)
        }
        setContentView(scroll)
    }
}
