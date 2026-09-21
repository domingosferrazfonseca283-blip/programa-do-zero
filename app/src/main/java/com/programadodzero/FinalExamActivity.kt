package com.programadodzero

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.*

class FinalExamActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra("language") ?: ProgressManager.getActiveLanguage(this) ?: "🐍  Python"
        val lessonIds = ContentRepository.lessonsFor(language).map { it.id }
        val completedLessons = ProgressManager.completedCount(this, language, lessonIds)
        val completedExercises = ProgressManager.completedExerciseCount(this, language, lessonIds)

        if (lessonIds.isEmpty() || completedLessons < lessonIds.size || completedExercises < lessonIds.size) {
            val root = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(32, 50, 32, 32)
                setBackgroundColor(Color.rgb(15, 23, 42))
            }
            root.addView(TextView(this).apply {
                text = "🔒 Avaliação final bloqueada"
                textSize = 27f
                setTextColor(Color.WHITE)
            })
            root.addView(TextView(this).apply {
                text = "Conclua todas as aulas e exercícios da trilha antes de fazer a avaliação final.\n\nAulas: $completedLessons/${lessonIds.size}\nExercícios: $completedExercises/${lessonIds.size\}"
                textSize = 18f
                setTextColor(Color.LTGRAY)
                setPadding(0, 22, 0, 30)
            })
            root.addView(Button(this).apply {
                text = "← Voltar"
                isAllCaps = false
                setOnClickListener { finish() }
            })
            setContentView(root)
            return
        }

        val questions = ContentRepository.finalExamFor(language)
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(28, 35, 28, 28); setBackgroundColor(Color.rgb(15,23,42)) }
        root.addView(TextView(this).apply { text = "🎓 Avaliação final"; textSize = 28f; setTextColor(Color.WHITE) })
        root.addView(TextView(this).apply { text = "Responda " + questions.size + " questões. É necessário atingir 70% para aprovação e liberar o certificado."; textSize = 17f; setTextColor(Color.LTGRAY); setPadding(0,15,0,20) })
        val choices = mutableListOf<RadioGroup>()
        questions.forEachIndexed { i, q ->
            root.addView(TextView(this).apply { text = (i + 1).toString() + ". " + q.question; textSize = 18f; setTextColor(Color.WHITE); setPadding(0,12,0,8) })
            val group = RadioGroup(this)
            q.options.forEachIndexed { j, option -> group.addView(RadioButton(this).apply { text = option; tag = j; setTextColor(Color.WHITE) }) }
            choices.add(group); root.addView(group)
        }
        val submit = Button(this).apply { text = "✅ Corrigir avaliação"; isAllCaps = false }; root.addView(submit)
        val result = TextView(this).apply { textSize = 18f; setTextColor(Color.WHITE); setPadding(0,20,0,20); gravity = Gravity.CENTER }; root.addView(result)
        submit.setOnClickListener {
            if (questions.isEmpty()) {
                result.text = "⚠️ Esta trilha ainda não possui questões para a avaliação final."
                submit.isEnabled = false
                return@setOnClickListener
            }

            val previousBest = ProgressManager.finalExamScore(this, language)
            ProgressManager.recordFinalExamAttempt(this, language)
            var score = 0
            questions.forEachIndexed { i, q ->
                val checked = choices[i].checkedRadioButtonId
                if (checked != -1 && choices[i].findViewById<RadioButton>(checked).tag == q.answerIndex) {
                    score++
                    ProgressManager.recordExamTopicScore(this, language, q.topic, true)
                } else {
                    ProgressManager.recordExamTopicScore(this, language, q.topic, false)
                }
            }

            val percent = score * 100 / questions.size
            val passed = ProgressManager.passFinalExam(this, language, score, questions.size)
            val newBest = previousBest == null || score > previousBest
            result.text = if (passed) {
                "🎉 APROVADO! $score/${questions.size} ($percent%)" +
                    (if (newBest) "\n+100 XP pelo novo melhor resultado" else "\nMelhor resultado mantido") +
                    "\nTentativas: ${ProgressManager.finalExamAttempts(this, language)}" +
                    "\n\nO certificado profissional está liberado no seu perfil."
            } else {
                "📚 $score/${questions.size} ($percent%)" +
                    "\nTentativas: ${ProgressManager.finalExamAttempts(this, language)}" +
                    "\n\nVocê precisa de pelo menos 70%. Revise as aulas e tente novamente."
            }
            if (passed) submit.isEnabled = false
        }
        root.addView(Button(this).apply { text = "← Voltar"; isAllCaps = false; setOnClickListener { finish() } })
        setContentView(ScrollView(this).apply { addView(root) })
    }
}