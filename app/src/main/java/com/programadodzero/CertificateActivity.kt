package com.programadodzero

import android.app.Activity
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CertificateActivity : Activity() {
    companion object {
        const val EXTRA_LANGUAGE = "language"
        const val EXTRA_STUDENT = "student"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val language = intent.getStringExtra(EXTRA_LANGUAGE) ?: ProgressManager.getActiveLanguage(this) ?: "🐍 Python"
        val student = intent.getStringExtra(EXTRA_STUDENT) ?: ProgressManager.getStudentName(this) ?: "Aluno"
        val lessonIds = ContentRepository.lessonsFor(language).map { it.id }

        if (!ProgressManager.isLanguageComplete(this, language, lessonIds)) {
            showLocked(language)
            return
        }

        val file = generatePdf(language, student)
        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 50, 36, 36)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        screen.addView(TextView(this).apply {
            text = "📜 Certificado profissional"
            textSize = 28f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        })
        screen.addView(TextView(this).apply {
            text = "✅ Curso concluído com sucesso.\\n\\nPDF criado offline.\\n\\nCódigo de verificação: \${verificationCode(language, student)}"
            textSize = 17f
            setTextColor(Color.WHITE)
            setPadding(0, 30, 0, 30)
        })
        screen.addView(Button(this).apply {
            text = "📄 Abrir PDF"
            isAllCaps = false
            setOnClickListener { openPdf(file) }
        })
        screen.addView(Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        })
        setContentView(screen)
    }

    private fun showLocked(language: String) {
        val screen = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 60, 36, 36)
            setBackgroundColor(Color.rgb(15, 23, 42))
        }
        screen.addView(TextView(this).apply {
            text = "🔒 Certificado ainda bloqueado"
            textSize = 26f
            setTextColor(Color.WHITE)
            setTypeface(null, Typeface.BOLD)
        })
        screen.addView(TextView(this).apply {
            text = "\\nConclua todas as aulas e exercícios de $language para liberar o certificado."
            textSize = 18f
            setTextColor(Color.LTGRAY)
        })
        screen.addView(Button(this).apply {
            text = "← Voltar"
            isAllCaps = false
            setOnClickListener { finish() }
        })
        setContentView(screen)
    }

    private fun generatePdf(language: String, student: String): File {
        val document = PdfDocument()
        val page = document.startPage(PdfDocument.PageInfo.Builder(842, 595, 1).create())
        val canvas = page.canvas
        canvas.drawColor(Color.WHITE)

        val border = Paint().apply { style = Paint.Style.STROKE; strokeWidth = 6f; color = Color.rgb(30, 41, 59) }
        canvas.drawRect(28f, 28f, 814f, 567f, border)
        val inner = Paint().apply { style = Paint.Style.STROKE; strokeWidth = 2f; color = Color.rgb(148, 163, 184) }
        canvas.drawRect(44f, 44f, 798f, 551f, inner)

        fun text(value: String, x: Float, y: Float, size: Float, bold: Boolean = false, color: Int = Color.rgb(30, 41, 59), align: Paint.Align = Paint.Align.CENTER) {
            val p = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                this.color = color
                textSize = size
                typeface = if (bold) Typeface.create(Typeface.SERIF, Typeface.BOLD) else Typeface.create(Typeface.SERIF, Typeface.NORMAL)
                textAlign = align
            }
            canvas.drawText(value, x, y, p)
        }

        text("PROGRAMAÇÃO DO ZERO", 421f, 100f, 24f, true)
        text("CERTIFICADO DE CONCLUSÃO", 421f, 145f, 30f, true)
        text("Certificamos que", 421f, 205f, 18f)
        text(student, 421f, 250f, 30f, true)
        text("concluiu a trilha completa de", 421f, 292f, 18f)
        text(language, 421f, 335f, 28f, true)
        text("incluindo aulas, exercícios práticos e avaliação da trilha.", 421f, 370f, 15f)
        text("Carga horária: formação prática offline", 421f, 392f, 12f, false, Color.DKGRAY)
        val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        text("Emitido em $date • Código \${verificationCode(language, student)}", 421f, 410f, 12f, false, Color.DKGRAY)

        val seal = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeWidth = 5f; color = Color.rgb(15, 118, 110) }
        canvas.drawCircle(150f, 455f, 58f, seal)
        canvas.drawCircle(150f, 455f, 47f, seal)
        drawQrLikeCode(canvas, verificationCode(language, student), 675f, 445f)
        text("CERTIFICADO", 150f, 451f, 11f, true, Color.rgb(15, 118, 110))
        text("CONCLUÍDO", 150f, 468f, 11f, true, Color.rgb(15, 118, 110))

        val line = Paint().apply { color = Color.rgb(71, 85, 105); strokeWidth = 1f }
        canvas.drawLine(335f, 480f, 510f, 480f, line)
        text("Programa de formação", 422f, 502f, 12f, false, Color.DKGRAY)

        document.finishPage(page)
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: filesDir
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "certificado-\${safe(language)}-\${verificationCode(language, student)}.pdf")
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        return file
    }

    private fun drawQrLikeCode(canvas: Canvas, code: String, left: Float, top: Float) {
        val paint = Paint().apply { color = Color.rgb(20, 30, 40); style = Paint.Style.FILL }
        val size = 8
        val matrix = Array(size) { BooleanArray(size) }
        val seed = MessageDigest.getInstance("SHA-256").digest(code.toByteArray())
        for (y in 0 until size) for (x in 0 until size) matrix[y][x] = ((seed[(y * size + x) % seed.size].toInt() xor x xor y) and 1) == 1
        for (y in 0 until size) for (x in 0 until size) if (matrix[y][x]) canvas.drawRect(left + x * 8f, top + y * 8f, left + x * 8f + 7f, top + y * 8f + 7f, paint)
    }

    private fun openPdf(file: File) {
        try {
            val uri = FileProvider.getUriForFile(this, "com.programadodzero.files", file)
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            })
        } catch (_: Exception) {
            // Se não houver leitor de PDF instalado, o arquivo continua salvo localmente.
        }
    }

    private fun safe(value: String): String = value.replace(Regex("[^a-zA-Z0-9_-]"), "_").take(40)

    private fun verificationCode(language: String, student: String): String {
        val raw = "$language|$student|programa-do-zero|certificate-v1"
        val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
        return digest.take(6).joinToString("") { "%02X".format(it) }
    }
}
