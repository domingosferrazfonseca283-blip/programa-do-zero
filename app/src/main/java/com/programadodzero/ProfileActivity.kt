package com.programadodzero

import android.app.Activity
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.app.AlertDialog
import android.content.Intent
import android.widget.EditText

class ProfileActivity : Activity() {
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val language = intent.getStringExtra("language") ?: ProgressManager.getActiveLanguage(this) ?: "🐍  Python"
        val xp = ProgressManager.getXp(this)
        val level = ProgressManager.getLevel(this)
        val levelXp = ProgressManager.xpIntoLevel(this)
        val reviews = ProgressManager.completedReviewCount(this, language)
        val streak = ProgressManager.registerStudyDay(this)
        val achievements = ProgressManager.achievements(this, language)
        val mission = ProgressManager.dailyMission(this, language)
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
            text = "⭐ XP: $xp\n🔥 Nível do programador: $level\n📊 XP do nível: $levelXp/100\n📝 Revisões acertadas: $reviews\n📚 Aulas concluídas: $lessons/$total\n🧩 Exercícios concluídos: $exercises/$total\n📈 Progresso: $percent%\n🔥 Sequência de estudo: $streak dias"
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

        val examScore = ProgressManager.finalExamScore(this, language)
        val examButton = Button(this).apply { text = if (examScore == null) "🎓 Fazer avaliação final" else "🎓 Avaliação final: " + examScore + "/10"; isAllCaps = false; setOnClickListener { startActivity(Intent(this@ProfileActivity, FinalExamActivity::class.java).apply { putExtra("language", language) }) } }
        screen.addView(examButton, LinearLayout.LayoutParams(-1, 60))

        if (complete) {
            val certificate = Button(this).apply {
                text = "📜 Gerar meu certificado profissional em PDF"
                isAllCaps = false
                setOnClickListener {
                    val existing = ProgressManager.getStudentName(this@ProfileActivity)
                    if (existing.isNullOrBlank()) {
                        val input = EditText(this@ProfileActivity).apply {
                            hint = "Nome completo do aluno"
                            setSingleLine(true)
                        }
                        AlertDialog.Builder(this@ProfileActivity)
                            .setTitle("Nome no certificado")
                            .setMessage("Digite o nome exatamente como deve aparecer no PDF.")
                            .setView(input)
                            .setPositiveButton("Gerar") { _, _ ->
                                val name = input.text.toString().trim()
                                if (name.isNotBlank()) {
                                    ProgressManager.setStudentName(this@ProfileActivity, name)
                                    openCertificate(language, name)
                                }
                            }
                            .setNegativeButton("Cancelar", null)
                            .show()
                    } else {
                        openCertificate(language, existing)
                    }
                }
            }
            screen.addView(certificate, LinearLayout.LayoutParams(-1, 60))
        }

        val back = Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        }

        screen.addView(title)
        screen.addView(subtitle)
        screen.addView(stats)
        val missionView = TextView(this).apply {
            text = "🎯 Missão de hoje\n\n${mission.title}\nProgresso: ${mission.progress}/${mission.target}\n🎁 Recompensa: +${mission.rewardXp} XP"
            textSize = 18f
            setTextColor(Color.WHITE)
            setPadding(0, 10, 0, 18)
        }
        screen.addView(missionView)
        if (mission.completed) {
            val claim = Button(this).apply {
                text = "🎁 Resgatar recompensa"
                isAllCaps = false
                setOnClickListener {
                    if (ProgressManager.claimDailyMission(this@ProfileActivity, language)) {
                        text = "✅ Recompensa resgatada"
                        isEnabled = false
                    }
                }
            }
            screen.addView(claim)
        }
        val badges = TextView(this).apply {
            text = if (achievements.isEmpty()) "🏅 Conquistas\n\nContinue estudando para desbloquear suas primeiras conquistas." else "🏅 Conquistas desbloqueadas\n\n" + achievements.joinToString("\n")
            textSize = 17f
            setTextColor(Color.WHITE)
            setPadding(0, 10, 0, 20)
        }
        screen.addView(badges)
        screen.addView(message, LinearLayout.LayoutParams(-1, 0, 1f))
        screen.addView(back, LinearLayout.LayoutParams(-1, 60))
        setContentView(screen)
    }

    private fun openCertificate(language: String, student: String) {
        startActivity(Intent(this, CertificateActivity::class.java).apply {
            putExtra(CertificateActivity.EXTRA_LANGUAGE, language)
            putExtra(CertificateActivity.EXTRA_STUDENT, student)
        })
    }
}