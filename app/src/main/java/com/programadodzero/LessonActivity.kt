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
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast

class LessonActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_LEVEL = "level"
        const val EXTRA_LEVEL_NUMBER = "level_number"
        const val EXTRA_MODULE = "module"
        const val EXTRA_LESSON_ID = "lesson_id"
        const val EXTRA_LESSON = "lesson"
    }

    private lateinit var progress: TextView
    private lateinit var title: TextView
    private lateinit var body: TextView
    private lateinit var code: TextView
    private lateinit var nextButton: Button
    private lateinit var review: TextView
    private lateinit var quizBox: LinearLayout
    private lateinit var lessons: List<LessonContent>
    private lateinit var language: String
    private lateinit var level: String
    private var levelNumber = 1
    private var moduleNumber = 1
    private var currentLesson = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        language = intent.getStringExtra(EXTRA_LANGUAGE) ?: "Linguagem"
        level = intent.getStringExtra(EXTRA_LEVEL) ?: "Nível"
        levelNumber = intent.getIntExtra(EXTRA_LEVEL_NUMBER, 1)
        moduleNumber = intent.getIntExtra(EXTRA_MODULE, 1)
        lessons = ContentRepository.lessonsForModule(language, levelNumber, moduleNumber)
        if (lessons.isEmpty()) {
            val message = TextView(this).apply {
                text = "📚 Este módulo ainda não possui aulas disponíveis.\n\nVolte e escolha outro módulo."
                textSize = 19f
                setTextColor(Color.WHITE)
                gravity = Gravity.CENTER
                setPadding(32, 48, 32, 48)
            }
            val back = Button(this).apply {
                text = "← Voltar aos módulos"
                isAllCaps = false
                gravity = Gravity.CENTER
                includeFontPadding = false
                setOnClickListener { finish() }
            }
            val emptyScreen = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(32, 40, 32, 32)
                setBackgroundColor(Color.rgb(15, 23, 42))
                addView(message, LinearLayout.LayoutParams(-1, 0, 1f))
                addView(back, LinearLayout.LayoutParams(-1, 64))
            }
            setContentView(emptyScreen)
            return
        }
        val requestedLessonId = intent.getStringExtra(EXTRA_LESSON_ID)
        currentLesson = if (requestedLessonId != null) lessons.indexOfFirst { it.id == requestedLessonId }.coerceAtLeast(0) else intent.getIntExtra(EXTRA_LESSON, firstIncompleteLesson())

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
        review = TextView(this).apply { textSize = 17f; setTextColor(Color.WHITE); setPadding(0,18,0,18) }
        quizBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0,10,0,20) }
        code = TextView(this).apply { textSize = 16f; setTextColor(Color.WHITE); setPadding(20,18,20,18); setBackgroundColor(Color.rgb(30,41,59)); typeface = Typeface.MONOSPACE }
        nextButton = Button(this).apply { textSize = 17f; isAllCaps = false }

        nextButton.setOnClickListener {
            if (!ProgressManager.isLessonCompleted(this, language, lessons[currentLesson].id)) {
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
        val lessonContent = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            addView(title)
            addView(body)
            addView(code, LinearLayout.LayoutParams(-1, -2))
            addView(review)
            addView(quizBox)
        }
        val scroll = ScrollView(this).apply { addView(lessonContent) }
        screen.removeView(title)
        screen.removeView(body)
        screen.removeView(code)
        screen.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(nextButton, LinearLayout.LayoutParams(-1, -2))
        screen.addView(backButton, LinearLayout.LayoutParams(-1, -2))
        setContentView(screen)
        showLesson()
    }

    override fun onResume() { super.onResume(); if (::lessons.isInitialized) showLesson() }

    private fun showLesson() {
        currentLesson = currentLesson.coerceIn(0, lessons.lastIndex)
        val lesson = lessons[currentLesson]
        val completed = ProgressManager.isLessonCompleted(this, language, lesson.id)
        val number = currentLesson + 1
        progress.text = "Aula $number de ${lessons.size} • ${((number - 1) * 100 / lessons.size)}% estudado"
        title.text = lesson.title
        body.text = lesson.body
        code.text = "Módulo ${lesson.module} • Exemplo:\n\n${lesson.code}"
        val objectives = lesson.objectives.joinToString("\n") { "• $it" }
        val points = lesson.keyPoints.joinToString("\n") { "• $it" }
        review.text = "🎯 OBJETIVOS\n$objectives\n\n📖 EXPLICAÇÃO\n${lesson.explanation.ifBlank { lesson.body }}\n\n💡 PONTOS-CHAVE\n$points\n\n🧠 REVISÃO\n${lesson.reviewQuestion}\n\nResposta: ${lesson.reviewAnswer}"
        quizBox.removeAllViews()
        val question = ContentRepository.reviewFor(language, lesson.id)
        if (question != null) {
            quizBox.addView(TextView(this).apply { text = "📝 Teste rápido"; textSize = 20f; setTextColor(Color.WHITE) })
            val options = RadioGroup(this)
            question.options.forEachIndexed { index, option ->
                options.addView(RadioButton(this).apply { text = option; textSize = 17f; setTextColor(Color.WHITE); id = 1000 + index })
            }
            quizBox.addView(options)
            quizBox.addView(Button(this).apply {
            gravity = Gravity.CENTER
            includeFontPadding = false
            setPadding(16, 10, 16, 10)
                text = "Verificar resposta"; isAllCaps = false
                setOnClickListener {
                    val selected = options.checkedRadioButtonId - 1000
                    if (selected < 0) Toast.makeText(this@LessonActivity, "Escolha uma resposta.", Toast.LENGTH_SHORT).show()
                    else if (selected == question.answerIndex) {
                        if (ProgressManager.recordReview(this@LessonActivity, language, question.id)) {
                            Toast.makeText(this@LessonActivity, "✅ Correto! +10 XP", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LessonActivity, "✅ Você já concluiu esta revisão.", Toast.LENGTH_SHORT).show()
                        }
                        isEnabled = false
                    } else Toast.makeText(this@LessonActivity, "❌ Ainda não. " + question.explanation, Toast.LENGTH_LONG).show()
                }
            })
        }
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
            putExtra(PracticeCodingActivity.EXTRA_LESSON_ID, lessons[currentLesson].id)
            putExtra(PracticeCodingActivity.EXTRA_LEVEL_NUMBER, levelNumber)
            putExtra(PracticeCodingActivity.EXTRA_MODULE, moduleNumber)
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
        (0 until lessons.size).firstOrNull { !ProgressManager.isLessonCompleted(this, language, lessons[it].id) } ?: lessons.lastIndex

}